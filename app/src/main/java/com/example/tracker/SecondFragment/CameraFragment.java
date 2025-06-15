package com.example.tracker.SecondFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.tracker.R;

import com.example.tracker.databinding.FragmentSecondCameraBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;

import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class CameraFragment extends Fragment {

    private FragmentSecondCameraBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSecondCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check and request permission when the fragment is created
        if (hasCameraPermission()) {
            bindCameraUseCases(getArguments());
        } else {
            requestPermission();
        }
    }

    // Checking if the camera permission is granted
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    // Requesting camera permission
    private void requestPermission() {
        requestPermissions(
                new String[]{android.Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE
        );
    }

    // Handling the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize the camera
                bindCameraUseCases(getArguments());
            } else {
                // Permission denied, show a Toast message
                Toast.makeText(
                        requireContext(),
                        "Camera permission required to use this feature",
                        Toast.LENGTH_LONG
                ).show();

                // Optionally guide the user to app settings
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    openAppSettings();
                }
            }
        }
    }

    // Initialize CameraX or other camera use cases
    private void bindCameraUseCases(Bundle arguments) {
        // Get the CameraProvider
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        // Add a listener to retrieve the CameraProvider asynchronously
        cameraProviderFuture.addListener(() -> {
            try {
                // Retrieve the ProcessCameraProvider without blocking
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Set up the Preview use case
                Preview previewUseCase = new Preview.Builder()
                        .build();

                // Attach the preview surface provider to the use case
                previewUseCase.setSurfaceProvider(binding.cameraView.getSurfaceProvider());


                BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();

                BarcodeScanner scanner = BarcodeScanning.getClient(options);

                ImageAnalysis analysisUseCase = new ImageAnalysis.Builder().build();

                analysisUseCase.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
                    processImageProxy(scanner, imageProxy, arguments);
                        });



                // Select the back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Bind the use case to the lifecycle
                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        previewUseCase,
                        analysisUseCase
                );

            } catch (ExecutionException | InterruptedException e) {
                // Handle exceptions if the cameraProviderFuture cannot complete
                e.printStackTrace();
                Log.e("WASDASDSAD", "Error accessing camera provider: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private String lastScannedBarcode = null;

    private boolean isProcessing = false;

    private void processImageProxy(BarcodeScanner barcodeScanner, ImageProxy imageProxy, Bundle arguments) {
        if (isProcessing) {
            imageProxy.close();
            return; // Verhindert parallele Verarbeitung
        }

        @OptIn(markerClass = ExperimentalGetImage.class)
        Image image = imageProxy.getImage();
        if (image != null) {
            isProcessing = true; // Verarbeite nur einen Barcode auf einmal
            InputImage inputImage = InputImage.fromMediaImage(
                    image,
                    imageProxy.getImageInfo().getRotationDegrees()
            );

            barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        if (barcodes.isEmpty()) {
                            Log.d("CameraFragment", "Kein Barcode gefunden.");
                            isProcessing = false;
                        } else {
                            String value = barcodes.get(0).getRawValue();

                            if (value.equals(lastScannedBarcode)) {
                                Log.d("CameraFragment", "Bereits verarbeiteter Barcode: " + value);
                                isProcessing = false;
                                return;
                            }

                            lastScannedBarcode = value;
                            Log.d("CameraFragment", "Neuer Barcode erkannt: " + value);

                            // Beispiel für die Nährwertabfrage
                            GetProductData.getProductsData(value, new GetProductData.ProductDataCallback() {
                                @Override
                                public void onSuccess(ArrayList<String> nutrition) {
                                    Log.d("CameraFragment", "Nährwerte: " + nutrition);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("0", nutrition.size() > 0 ? nutrition.get(0) : "N/A");
                                    bundle.putString("1", nutrition.size() > 1 ? nutrition.get(1) : "N/A");
                                    bundle.putString("2", nutrition.size() > 2 ? nutrition.get(2) : "N/A");
                                    bundle.putString("3", nutrition.size() > 3 ? nutrition.get(3) : "N/A");
                                    bundle.putString("4", nutrition.size() > 4 ? nutrition.get(4) : "N/A");
                                    bundle.putString("5", nutrition.size() > 5 ? nutrition.get(5) : "N/A");
                                    bundle.putString("6", nutrition.size() > 6 ? nutrition.get(6) : "N/A");


                                    bundle.putBoolean("camera_complete", true); // Beispiel-Ergebnis
                                    getParentFragmentManager().setFragmentResult("camera_result", bundle);

                                    requireActivity().runOnUiThread(() -> {
                                                NavController navController = NavHostFragment.findNavController(CameraFragment.this);
                                                if(arguments.getString("1").equals("Second")){
                                                    navController.navigate(R.id.action_CameraFragment_to_SecondFragment, bundle);
                                                }else{
                                                    navController.navigate(R.id.action_CameraFragment_to_FirstFragment, bundle);

                                                }

                                    });
                                    isProcessing = false;
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Log.e("CameraFragment", "Fehler: " + errorMessage);
                                    isProcessing = false;
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CameraFragment", "Fehler beim Scannen: " + e.getMessage());
                        isProcessing = false;
                    })
                    .addOnCompleteListener(task -> {
                        // WICHTIG: Schließt das ImageProxy, damit neue Frames verarbeitet werden können
                        imageProxy.close();
                    });
        } else {
            imageProxy.close(); // Falls das Bild null ist, ebenfalls schließen
        }
    }
    // Open the app settings if permission is permanently denied


    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

//    private void processImageProxyy(BarcodeScanner barcodeScanner, ImageProxy imageProxy) {
//
//        @OptIn(markerClass = ExperimentalGetImage.class) Image image = imageProxy.getImage();
//        InputImage inputImage = null;
//        if (image != null) {
//            inputImage = InputImage.fromMediaImage(
//                    image,
//                    imageProxy.getImageInfo().getRotationDegrees()
//            );
//            // Weiterverarbeitung von inputImage
//        }
//
//        barcodeScanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
//                    @Override
//                    public void onSuccess(List<Barcode> barcodes) {
//                        if (barcodes.isEmpty()) {
//                            Toast.makeText(requireContext(), "Kein Barcode gefunden", Toast.LENGTH_SHORT).show();
//                            Log.e("SecondFragment", "Was ein scheiss");
//                        }
//
//                        for (Barcode barcode : barcodes) {
//                            String value = barcode.getRawValue();
//                            //binding.bottomText.setText(binding.bottomText.getText() + value);
//
//                            Log.d("SecondFragment", "Barcode gefunden: " + value);
//                            Toast.makeText(requireContext(), "Barcode: " + value, Toast.LENGTH_SHORT).show();
//
//
//                            NavHostFragment.findNavController(CameraFragment.this)
//                                    .navigate(R.id.action_CameraFragment_to_SecondFragment);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("SecondFragment", "Fehler beim Scannen: " + e.getMessage());
//                        Toast.makeText(requireContext(), "Fehler beim Scannen", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnCompleteListener(task -> {
//                    // WICHTIG: Schließt das ImageProxy, damit neue Frames verarbeitet werden können
//                    imageProxy.close();
//                });
//    }
}
