package com.example.tracker.ThirdFragment;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tracker.R;
import android.content.Context;
import com.example.tracker.databinding.FragmentThirdTableGraphBinding;

import java.util.ArrayList;

public class TableFragment extends Fragment{

    private FragmentThirdTableGraphBinding binding;

    private Context context;
    TableLayout tableLayout;
    int counterRow = 2;
    int counterColumn = 3;
    SharedViewModel sharedViewModel;
    DataRepository dataRepository;
    //TableFragment tableFragment;
    String databaseName;
    DatabaseCards databaseCards;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentThirdTableGraphBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseCards = new DatabaseCards(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            int cardId = args.getInt("cardId");
            this.databaseName = args.getString("cardName");

            // Daten verwenden
            Log.d("TableFragment", "Card ID: " + cardId + ", Name: " + this.databaseName);
        }


        HorizontalScrollView horizontalScrollView = binding.horizontalScrollView;
        horizontalScrollView.setBackgroundResource(R.drawable.table_first_row);

        // Initialize Views and Variables
        tableLayout = binding.tableLayout;

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dataRepository = new DataRepository(getContext(), this.databaseName);
        sharedViewModel.SetDatabaseName(this.databaseName);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(databaseName);
        titleTextView.setTextSize(30);
        titleTextView.setTextColor(getResources().getColor(R.color.text_color));

        // Retrieve saved row and column counts from SharedPreferences
        loadCounterValues();

        // Load existing table data from the repository
        loadExistingTable();

        // Set listeners for adding rows and columns
        binding.addRowButton.setOnClickListener(v -> {
            addRow(counterRow, counterColumn);
            counterRow++;
            sharedViewModel.setRowCount(counterRow - 1);
            saveCounterValues(dataRepository); // Save the updated row count
            updateTableDataInViewModel();
        });

        binding.addColumnButton.setOnClickListener(v -> {
            addColumn(counterColumn);
            counterColumn++;
            sharedViewModel.setColumnCount(counterColumn);
            saveCounterValues(dataRepository); // Save the updated column count
            updateTableDataInViewModel();
        });

        binding.timerButton.setOnClickListener(v -> {
//            dataRepository.clearTableData();
//            counterRow = 2;
//            counterColumn = 3;
//            sharedViewModel.setRowCount(counterRow);
//            sharedViewModel.setColumnCount(counterColumn);
//            saveCounterValues(dataRepository); // Reset the saved values to default
//            loadExistingTable();
            TimerSheet timerSheet = new TimerSheet(requireContext());
            timerSheet.start();

        });
    }

    private void saveCounterValues(DataRepository data) {
        SharedPreferences preferences = requireContext().getSharedPreferences("table_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(databaseName + "counterRow", counterRow);
        editor.putInt(databaseName + "counterColumn", counterColumn);
        editor.apply();
    }

    private void loadCounterValues() {
        SharedPreferences preferences = requireContext().getSharedPreferences("table_preferences", Context.MODE_PRIVATE);
        counterRow = preferences.getInt(databaseName + "counterRow", 2); // Default to 2 if not set
        counterColumn = preferences.getInt(databaseName + "counterColumn", 3); // Default to 3 if not set
    }

    void loadExistingTable() {
        ArrayList<ArrayList<String>> savedTableData = dataRepository.loadTableData();
        Log.d("loadExistingTable", "savedTableData: " + savedTableData);

        if (savedTableData == null || savedTableData.size() < 3) {
            Log.e("ThirdFragment", "Nicht genügend Daten in savedTableData vorhanden.");
            return;
        }

        View fragmentView = getView();
        EditText cell0 = fragmentView.findViewById(R.id.uebung_1); // Replace with your actual ID
        EditText cell1 = fragmentView.findViewById(R.id.uebung_2); // Replace with your actual ID
        EditText cell2 = fragmentView.findViewById(R.id.uebung_3); // Replace with your actual ID

        cell0.setText(savedTableData.get(0).get(0));
        cell1.setText(savedTableData.get(0).get(1));
        cell2.setText(savedTableData.get(0).get(2));

        textAnpassung(cell0);
        textAnpassung(cell1);
        textAnpassung(cell2);

        EditText editText1 = fragmentView.findViewById(R.id.editTextNumber1); // Replace with your actual ID
        EditText editText2 = fragmentView.findViewById(R.id.editTextNumber2); // Replace with your actual ID
        EditText editText3 = fragmentView.findViewById(R.id.editTextNumber3); // Replace with your actual ID

        editText1.setText(savedTableData.get(1).get(0));
        editText2.setText(savedTableData.get(1).get(1));
        editText3.setText(savedTableData.get(1).get(2));

        textAnpassung(editText1);
        textAnpassung(editText2);
        textAnpassung(editText3);


        View reiheView0 = tableLayout.getChildAt(0);
        TableRow reihen = (TableRow) reiheView0;

        View reiheView1 = tableLayout.getChildAt(1);
        TableRow reihe1 = (TableRow) reiheView1;
        reihe1.setBackgroundResource(R.drawable.table_border_2);


        for(int col = 3; col<savedTableData.get(2).size(); col++){
            EditText editText = new EditText(getContext());
            textAnpassung(editText);
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            editText.setMaxLines(3);
            editText.setWidth(300);
            //editText.setHeight(50);
            editText.setText(savedTableData.get(0).get(col)); // Setze den Text für die neue Spalte
            editText.setTextColor(getResources().getColor(R.color.text_color));
            editText.setGravity(Gravity.CENTER);
            editText.setBackground(null);


            reihen.addView(editText);
            addCell(reihe1,1,col);
        }




        for(int row = 2; row < savedTableData.size(); row++){
            addRow(row,savedTableData.get(row).size());
        }

//        sharedViewModel.setRowCount(counterRow);
//        sharedViewModel.setColumnCount(counterColumn);
    }

    void addRow(int tag, int spalte) {
        TableRow newRow = new TableRow(getContext());
        EditText editText = new EditText(getContext());
        TextView newTextView = new TextView(getContext());

        if ( (tag & 1) == 0 ){
            //newRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.zeile1));
            newRow.setBackgroundResource(R.drawable.table_border_1);

        }else{
            //newRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.zeile2));
            newRow.setBackgroundResource(R.drawable.table_border_2);

        }

        //Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/your_font.ttf");
        newTextView.setText("Day " +tag);
        newTextView.setPadding(8,0,0,0);
        newTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
        newTextView.setTypeface(newTextView.getTypeface(), Typeface.BOLD);

        newRow.addView(newTextView);


        for (int col = 0; col < spalte; col++) {
            addCell(newRow, tag, col);
        }

        databaseCards.updateDays(this.databaseName, tag);

        tableLayout.addView(newRow);
    }

    private void addCell(TableRow tableRow, int row, int spalte){
        ArrayList<ArrayList<String>> savedTableData = dataRepository.loadTableData();
        Log.d("loadExistingTable", "savedTableData neu: " + savedTableData);


//        if (row >= savedTableData.size() || spalte >= savedTableData.get(row).size()) {
//            Log.e("ThirdFragment", "Index out of bounds in savedTableData: row=" + row + ", column=" + spalte);
//            return;
//        }

        EditText editText = new EditText(getContext());
        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL); // Dezimalzahlen erlauben
        editText.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
        editText.setGravity(Gravity.CENTER);
        editText.setWidth(300);

        String etwas = "0" ;

        try {
            // Versuche, den gespeicherten Wert zu laden
            if (!"0".equals(savedTableData.get(row).get(spalte))) {
                etwas = savedTableData.get(row).get(spalte);
            }
        } catch (IndexOutOfBoundsException e) {
            // Wenn IndexOutOfBoundsException, bleibt der Wert "0"
            Log.e("ThirdFragment", "IndexOutOfBoundsException in addCell: row=" + row + ", column=" + spalte);
        }

        editText.setText(etwas);
//        editText.setHintTextColor(getResources().getColor(R.color.text_color));
        editText.setSingleLine(true);
        editText.setBackground(null);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateTableDataInViewModel(); // Update data in ViewModel whenever the cell changes
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        tableRow.addView(editText);
    }

    public void addColumn(int col){
        // Durch jede Zeile iterieren
        EditText editText = new EditText(getContext());
        textAnpassung(editText);
        editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setMaxLines(3);
        editText.setWidth(300);
        editText.setText("Exercise " + (col + 1)); // Setze den Text für die neue Spalte
        editText.setTextColor(getResources().getColor(R.color.text_color));
        editText.setGravity(Gravity.CENTER);
        editText.setBackground(null);
        //editText.setHeight(50);
        View reiheView = tableLayout.getChildAt(0);
        TableRow reihen = (TableRow) reiheView;
        reihen.addView(editText);

        for (int i = 1; i < tableLayout.getChildCount(); i++) {

            View rowView = tableLayout.getChildAt(i);

            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                addCell(row,i,col);
            }
        }
        databaseCards.updateExercises(this.databaseName, col+1 );
    }

    void updateTableDataInViewModel() {
        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            ArrayList<String> rowData = new ArrayList<>();
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                View cell = row.getChildAt(j+1);
                String cellValue = "";
                if (cell instanceof EditText) {
                    cellValue = ((EditText) cell).getText().toString();
                } else if (cell instanceof TextView) {
                    cellValue = ((TextView) cell).getText().toString();
                }
                rowData.add(cellValue);
            }
            tableData.add(rowData);
        }
        sharedViewModel.setTableData(tableData); // Tabelle an ViewModel übergeben
    }

    void textAnpassung(EditText editText){
//        editText.setSingleLine(false); // Allow multi-line input
//        editText.setMaxLines(3); // Limit to 3 lines to prevent row expansion
//        editText.setHeight(150); // Set a fixed height for the EditText
//        editText.setEllipsize(null); // Prevent ellipsis; let text wrap
//        editText.setGravity(Gravity.TOP | Gravity.START); // Align text to the top-left
//        editText.setHorizontallyScrolling(false); // Ensure text wraps within the view
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTableDataInViewModel();
                // Setze die Schriftgröße basierend auf der Länge des Textes
                if (s.length() > 20) { // Passe die Zahl hier nach Bedarf an
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                } else if (s.length() > 10) {
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                } else {
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}