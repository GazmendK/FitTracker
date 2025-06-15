package com.example.tracker.ThirdFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;
import com.example.tracker.databinding.FragmentThirdBinding;
import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.*;

public class ThirdFragment extends Fragment{

    private FragmentThirdBinding binding;

    private Context context;
    TableLayout tableLayout;
    int counterRow = 2;
    int counterColumn = 3;
    SharedViewModel sharedViewModel;
    DataRepository dataRepository;
    TableFragment tableFragment;

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<ArrayList<String>> plans;
    private DatabaseCards databaseCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisieren von RecyclerView und FloatingActionButton
        recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton addButton = view.findViewById(R.id.add_button);

        // Initialisieren der Item-Liste und des Adapters
        plans = new ArrayList<>();
        databaseCards = new DatabaseCards(getContext());

        adapter = new CardAdapter(plans, position -> {
            Bundle bundle = new Bundle();
            bundle.putInt("cardId", position);// Hier wird die Navigation gestartet
            bundle.putString("cardName", plans.get(position).get(1));


            NavHostFragment.findNavController(ThirdFragment.this)
                    .navigate(R.id.action_ThirdFragment_to_TableGraph, bundle);
        }, position -> {
            deletePlanSheet(plans.get(position).get(1));
        });

        loadExistingCards();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


//        view.findViewById(R.id.deleteCards).setOnClickListener(v -> databaseCards.clearTableData());

        addButton.setOnClickListener(v -> {
//            String newItem = "Trainingsplan " + (plans.size() + 1);
//
//            databaseCards.insertValue(newItem,0,0);
//
//            //adapter.addItem(newItem, plans.size());
//            databaseCards.insertValue(newItem,0,0);
//            adapter.addItem(""+plans.size(),newItem,"0","0");
            openSheet(plans.size());
        });




    }

    private void deletePlanSheet(String name) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View view1 = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_third_delete_plan_sheet, null);
        bottomSheetDialog.setContentView(view1);

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view1.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);  // Sicherstellen, dass es an den Inhalt angepasst wird
        bottomSheetDialog.show();




        view1.findViewById(R.id.deletePlan).setOnClickListener(v -> {
//            String name =  editName.getText().toString().trim();
//
//            databaseCards.insertValue(name,0,0);
//            adapter.addItem(""+position,name,"0","0");
            databaseCards.deleteTrainingPlan(name);
            bottomSheetDialog.dismiss();

        });
    }

    void loadExistingCards(){
        ArrayList<ArrayList<String>> savedTableData = databaseCards.loadTableData();

        String newItem;
        for(int i=0; i<savedTableData.size(); i++){
            //newItem = "Trainingsplan " + (savedTableData.get(i).getKey() + 1);
            ArrayList<String> plan = savedTableData.get(i);
            adapter.addItem(plan.get(0), plan.get(1),plan.get(2),plan.get(3));
        }


    }

    private void openSheet(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View view1 = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_third_create_plan_sheet, null);
        bottomSheetDialog.setContentView(view1);

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view1.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);  // Sicherstellen, dass es an den Inhalt angepasst wird
        bottomSheetDialog.show();

        TextInputEditText editName = view1.findViewById(R.id.editTextPlanName);



        view1.findViewById(R.id.submitName).setOnClickListener(v -> {
            String name =  editName.getText().toString().trim();
            dataRepository = new DataRepository(requireContext(), name);

            databaseCards.insertValue(name,3,1);
            adapter.addItem(""+position,name, String.valueOf(3) ,String.valueOf(1));

            bottomSheetDialog.dismiss();

        });




    }

    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Tracker");

        //for()


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}