package com.example.tracker.ThirdFragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.tracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    private LineChart lineChart;
    private DataRepository dataRepository;

    private SharedViewModel sharedViewModel;
    private ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
    private int ColumnCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_graph, container, false);
        lineChart = view.findViewById(R.id.weightLineChart);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        dataRepository = new DataRepository(getContext(), sharedViewModel.getDatabaseName().getValue());
        setupLineChart();




        ArrayList<ArrayList<String>> savedData = dataRepository.loadTableData();
        sharedViewModel.setTableData(savedData);

        sharedViewModel.getTableData().observe(getViewLifecycleOwner(), tableData -> {
            saveTableDataToDatabase(tableData);

            updateChartData(tableData);
        });

    }

    private void setupLineChart() {
        lineChart.getAxisLeft().setEnabled(true); // Linke Y-Achse aktivieren
        lineChart.getAxisRight().setEnabled(false); // Rechte Y-Achse deaktivieren

        // Nur die untere X-Achse sichtbar machen
        lineChart.getXAxis().setEnabled(true); // Untere X-Achse aktivieren
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        int white = getResources().getColor(R.color.text_color);

        lineChart.getAxisLeft().setGridColor(white); // weil wir ja oben AxisRight deaktiviert haben(recht y achse)
        lineChart.getAxisLeft().setAxisLineColor(white);
        lineChart.getAxisLeft().setTextColor(white);

        lineChart.getXAxis().setGridColor(white);
        lineChart.getXAxis().setAxisLineColor(white);
        lineChart.getXAxis().setTextColor(white);

        lineChart.getDescription().setText("");


        // Weitere Diagramm-Konfiguration
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setWordWrapEnabled(true);

// Position der Legende unten festlegen
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false); // Außerhalb des Charts platzieren

    }

    private void saveTableDataToDatabase(ArrayList<ArrayList<String>> tableData) {
        dataRepository.clearTableData();  // Entfernt alte Daten
        for (int row = 0; row < tableData.size(); row++) {
            for (int col = 0; col < tableData.get(row).size(); col++) {
                String value = tableData.get(row).get(col);
                if (value != null && !value.isEmpty()) {
                    dataRepository.insertValue(row, col, value);  // Speichert neuen Wert
                }
            }
        }
    }

    private void updateChartData(ArrayList<ArrayList<String>> tableData) {
        if (tableData == null || tableData.isEmpty() || tableData.get(0).isEmpty()) return;

        int[] colors = {
                Color.parseColor("#0090d3"),
                Color.parseColor("#8689e5"),
                Color.parseColor("#da77d2"),
                Color.parseColor("#ff6a9f"),
                Color.parseColor("#ff7c5d"),
                Color.parseColor("#ffa600")
        };

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        int maxColumns = tableData.get(0).size(); // Anzahl der Spalten

        for (int col = 0; col < maxColumns; col++) {

            String columnTitle = tableData.get(0).get(col);
            if (columnTitle == null || columnTitle.isEmpty()) {
                continue; // Überspringe leere oder ungültige Spalten
            }

            List<Entry> entries = new ArrayList<>();
            for (int row = 1; row < tableData.size(); row++) { // Beginne bei Zeile 1
                if (tableData.get(row).size() > col) { // Prüfe, ob Spalte existiert
                    String cellValue = tableData.get(row).get(col);
                    if (cellValue != null && !cellValue.isEmpty()) {
                        try {
                            float yValue = Float.parseFloat(cellValue);
                            entries.add(new Entry(row, yValue));
                        } catch (NumberFormatException ignored) {
                            // Ignoriere ungültige Zahlen
                        }
                    }
                }
            }
            int colorIndex = col % colors.length; // Schleife über die Farbpalette
            LineDataSet lineDataSet = new LineDataSet(entries, columnTitle);
            lineDataSet.setColor(colors[colorIndex]);
            lineDataSet.setCircleColor(0);
            lineDataSet.setCircleHoleColor(0);
            lineDataSet.setValueTextColor(0); // Farbe für die Punkte
            //lineDataSet.setValueTextColor(getResources().getColor(R.color.text_color)); // Textfarbe
            lineDataSet.setLineWidth(1.5F);
            lineDataSets.add(lineDataSet);
        }

        lineChart.setData(new LineData(lineDataSets));
        lineChart.invalidate(); // Aktualisiere das Diagramm
    }



    private void updateChartDataa(ArrayList<ArrayList<String>> tableData) {

        if (tableData == null || tableData.isEmpty() || tableData.get(0).isEmpty()) {
            // Wenn keine Daten vorhanden sind, beende die Methode
            return;
        }

        lineDataSets.clear(); // Clear existing data sets

        for (int col = 1; col < tableData.get(0).size(); col++) {
            ArrayList<Entry> entries = new ArrayList<>();

            for (int row = 1; row < tableData.size(); row++) {
                float yValue = Float.parseFloat(tableData.get(row).get(col));
                entries.add(new Entry(row, yValue));
            }

            // Create LineDataSet for each column
            LineDataSet lineDataSet = new LineDataSet(entries, "Column " + (col + 1));
            int color = Color.rgb(50 + col * 33, 100, 130);
            lineDataSet.setColor(color); // Different color per column
            lineDataSet.setValueTextColor(getResources().getColor(R.color.text_color)); // Textfarbe
            lineDataSet.setCircleColors(color);
            lineDataSet.setLineWidth(2f);

            lineDataSets.add(lineDataSet); // Add to list of datasets
        }

        // Apply all datasets to the LineChart
        List<ILineDataSet> iLineDataSets = new ArrayList<>(lineDataSets);
        LineData lineData = new LineData(iLineDataSets);;
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }

    private void hinzufuegen (LineDataSet lineDataSet){
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Beschreibung und Animationen
        Description description = new Description();
        description.setText("Meine Daten über Zeit");
        lineChart.setDescription(description);

        lineChart.animateX(1000); // Animation für den Start

        lineChart.invalidate();
    }



}
