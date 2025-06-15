package com.example.tracker.SecondFragment;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;
import com.example.tracker.databinding.FragmentSecondBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private NutritionDatabase nutritionDatabase;
    private MaxNutritionsData maxNutritionsData;
    DailyOverview dailyOverview;
    private RecyclerView recyclerView;
    private ProductCardAdapter adapter;
    View includedView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nutritionDatabase = new NutritionDatabase(requireContext());
        maxNutritionsData = new MaxNutritionsData(requireContext());

        recyclerView = view.findViewById(R.id.ProductRecyclerView);
        adapter = new ProductCardAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        includedView = getView().findViewById(R.id.includeDailyOverview);

        dailyOverview = new DailyOverview(requireContext(),view, nutritionDatabase, maxNutritionsData, includedView, adapter);

        includedView.findViewById(R.id.nutritionSettings).setOnClickListener(v -> dailyOverview.settings());

        dailyOverview.writeMaxNutritions();

        loadSavedProducts(); // Für die Graphen


        dailyOverview.loadNutritionCardView(); //Für die cards dadrunter

        binding.barcodeScanner.setOnClickListener(v -> test());




        //binding.nutritionSettings.setOnClickListener(v -> settings());

        getParentFragmentManager().setFragmentResultListener("camera_result", this, (requestKey, result) -> {
            boolean isCameraComplete = result.getBoolean("camera_complete", false);
            if (isCameraComplete) {
                dailyOverview.bottomSheet(getArguments());
            }
        });


//        binding.delete.setOnClickListener(v -> {
//            nutritionDatabase.clearTableData();
//            adapter.setItems(new ArrayList<>());
//        });

    }






    private void loadSavedProducts() {

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(currentDate);

        ArrayList<ArrayList<String>> savedProducts = new ArrayList<>();

        for (int i = nutritionDatabase.loadNutritionData().size() - 1; i >= 0; i--) {
            ArrayList<String> item = nutritionDatabase.loadNutritionData().get(i);
            if (item.get(0).equals(formattedDate)) {
                try{
                    savedProducts.add(item);
                }catch (NumberFormatException e) {
                    Log.e("DailyOverview", "Fehler beim Parsen von Kalorien: " + item.get(2), e);
                }
            }
        }

//        for(int i=0; i<nutritionDatabase.loadNutritionData().size(); i++){
//            if(nutritionDatabase.loadNutritionData().get(i).get(0).equals(formattedDate)){
//                savedProducts.add(nutritionDatabase.loadNutritionData().get(i));
//            }
//        }

//        ArrayList<ArrayList<String>> savedProducts = nutritionDatabase.loadNutritionData(); // Methode in der DB erstellen
        adapter.setItems(savedProducts); // Geladene Produkte an den Adapter übergeben
    }


    private void test(){

        Bundle bundle = new Bundle();
        bundle.putString("1", "Second");

        NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_CameraFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Nutritions");
        dailyOverview.loadNutritionCardView();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}