package com.example.tracker.FirstFragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;
import com.example.tracker.SecondFragment.*;
import com.example.tracker.databinding.FragmentFirstBinding;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecyclerView recyclerView;
    View includedView;
    DailyOverview dailyOverview;
    WeightDatabase weightDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weightDatabase = new WeightDatabase(requireContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();


        NutritionDatabase nutritionDatabase = new NutritionDatabase(requireContext());
        MaxNutritionsData maxNutritionsData = new MaxNutritionsData(requireContext());


        ProductCardAdapter adapter = new ProductCardAdapter();

        includedView = getView().findViewById(R.id.includeDailyOverview);

        dailyOverview = new DailyOverview(requireContext(),view, nutritionDatabase, maxNutritionsData, includedView, adapter);

        includedView.findViewById(R.id.nutritionSettings).setOnClickListener(v -> dailyOverview.settings());

        dailyOverview.writeMaxNutritions();
        dailyOverview.loadNutritionCardView(); //Für die cards dadrunter

        getParentFragmentManager().setFragmentResultListener("camera_result", this, (requestKey, result) -> {
            boolean isCameraComplete = result.getBoolean("camera_complete", false);
            if (isCameraComplete) {
                dailyOverview.bottomSheet(getArguments());
            }
        });

        binding.barcodeScanner.setOnClickListener(v -> test());


    }

    private void test(){

        Bundle bundle = new Bundle();
        bundle.putString("1", "First");

        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_CameraFragment, bundle);
    }

    private void openSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View view1 = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_first_weight_sheet, null);
        bottomSheetDialog.setContentView(view1);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view1.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);  // Sicherstellen, dass es an den Inhalt angepasst wird
        bottomSheetDialog.show();

        TextInputEditText etwas = view1.findViewById(R.id.editTextWeight);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(currentDate);

        view1.findViewById(R.id.submitWeight).setOnClickListener(v -> {
            weightDatabase.insertValue(formattedDate, Double.parseDouble(Objects.requireNonNull(etwas.getText()).toString()));
            bottomSheetDialog.dismiss();
            setupRecyclerView();
        });

        view1.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            // Sichtbaren Bereich des Viewports abrufen
            view1.getWindowVisibleDisplayFrame(rect);
            int screenHeight = view1.getRootView().getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            // Wenn die Tastatur sichtbar ist
            if (keypadHeight > screenHeight * 0.15) {
                // Tastatur ist sichtbar, das Bottom Sheet nach oben verschieben
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);  // BottomSheet öffnen
            } else {
                // Tastatur ist nicht sichtbar
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);  // BottomSheet zusammenfalten
            }
        });




    }

    private void setupRecyclerView() {
        // Sample graph data
        List<List<Entry>> graphData = new ArrayList<>();
        ArrayList<ArrayList<String>> loadedWeight = weightDatabase.loadWeightData();
        int start = 0;

        List<Entry> graph1 = new ArrayList<>();

        if(loadedWeight.size() > 90){
            start = loadedWeight.size()-90;
        }
        for(int i = start; i<loadedWeight.size(); i++){
            graph1.add(new Entry(i, Float.parseFloat(loadedWeight.get(i).get(1))));
        }



        graphData.add(graph1);

        // Graph 2
        List<Entry> graph2 = new ArrayList<>();
        for(int i=0; i<30; i++){
            int random = (int) (Math.random()*(12000-6000+1)) + 6000;
            graph2.add(new Entry(i,random));
        }
//        graph2.add(new Entry(0, 8000));
//        graph2.add(new Entry(1, 9000));
//        graph2.add(new Entry(2, 6000));
        graphData.add(graph2);

        // Setup RecyclerView
        GraphCardAdapter adapter = new GraphCardAdapter(requireContext(), graphData, position -> {
            // Hier öffnest du das Sheet oder machst etwas anderes
            openSheet();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                SpannableString spannableString = new SpannableString("FitTracker");
                spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
                actionBar.setTitle(spannableString);
            }
        }
    }

}