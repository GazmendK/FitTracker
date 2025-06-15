package com.example.tracker.FirstFragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;
import java.util.List;

public class GraphCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<List<Entry>> graphData;
    private static final int TYPE_LINE_CHART = 0;
    private static final int TYPE_BAR_CHART = 1;
    private final OnAddWeightClickListener addWeightClickListener;

    public GraphCardAdapter(Context context, List<List<Entry>> graphData, OnAddWeightClickListener addWeightClickListener) {
        this.context = context;
        this.graphData = graphData;
        this.addWeightClickListener = addWeightClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        // Alternativ: Du kannst eine zusätzliche Liste oder Logik hinzufügen,
        // um den Typ pro Position zu bestimmen
        return position % 2 == 0 ? TYPE_LINE_CHART : TYPE_BAR_CHART;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LINE_CHART) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_first_weight_graph, parent, false);
            return new LineChartViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_first_steps_graph, parent, false);
            return new BarChartViewHolder(view, context);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        View.OnClickListener addWeightListener = v -> {
            addWeightClickListener.onAddWeightClicked(position);
        };

        if (holder.getItemViewType() == TYPE_LINE_CHART) {
            ((LineChartViewHolder) holder).bind(graphData.get(position),addWeightListener);

        } else {
            ((BarChartViewHolder) holder).bind(graphData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return graphData.size();
    }


    static class LineChartViewHolder extends RecyclerView.ViewHolder {
        private final LineChart lineChart;
        private final Context context;
        private final View addWeightButton;

        public LineChartViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            lineChart = itemView.findViewById(R.id.weightLineChart);
            addWeightButton = itemView.findViewById(R.id.addWeight);
            this.context = context;
        }

        void bind(List<Entry> data, View.OnClickListener addWeightListener) {
            LineDataSet dataSet = new LineDataSet(data, "Line Chart");
            dataSet.setColor(context.getColor(R.color.carbs));
            dataSet.setLineWidth(1.5f);
            dataSet.setCircleColor(0);
            dataSet.setCircleHoleColor(0);
            dataSet.setValueTextColor(0);
            LineData lineData = new LineData(dataSet);
            setupLineChart();


            lineChart.setData(lineData);

            lineChart.invalidate();

            // Set OnClickListener for the addWeight button
            addWeightButton.setOnClickListener(addWeightListener);
        }

        private void setupLineChart() {
            lineChart.setHighlightPerTapEnabled(false); // Deaktiviert die Highlight-Linien beim Tippen
            lineChart.setHighlightPerDragEnabled(false);
            lineChart.getAxisLeft().setEnabled(true); // Linke Y-Achse aktivieren
            lineChart.getAxisRight().setEnabled(false); // Rechte Y-Achse deaktivieren

            // Nur die untere X-Achse sichtbar machen
            lineChart.getXAxis().setEnabled(true); // Untere X-Achse aktivieren
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


            int white = ContextCompat.getColor(context, R.color.text_color);

            lineChart.getAxisLeft().setGridColor(white); // weil wir ja oben AxisRight deaktiviert haben(recht y achse)
            lineChart.getAxisLeft().setAxisLineColor(white);
            lineChart.getAxisLeft().setTextColor(white);

            lineChart.getXAxis().setGridColor(white);
            lineChart.getXAxis().setAxisLineColor(white);
            lineChart.getXAxis().setTextColor(white);

            lineChart.getDescription().setText("");

            // Weitere Diagramm-Konfiguration
            Legend legend = lineChart.getLegend();
            legend.setEnabled(false);


        }

    }

    static class BarChartViewHolder extends RecyclerView.ViewHolder {
        private final BarChart barChart;
        private final Context context;

        public BarChartViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            barChart = itemView.findViewById(R.id.stepsBarChart);
            this.context = context;
        }

        void bind(List<Entry> data) {
            List<BarEntry> barEntries = new ArrayList<>();
            for (Entry entry : data) {
                barEntries.add(new BarEntry(entry.getX(), entry.getY()));
            }
            BarDataSet dataSet = new BarDataSet(barEntries, "Bar Chart");
            dataSet.setColor(context.getColor(R.color.calorie));

            dataSet.setValueTextColor(0);
            BarData barData = new BarData(dataSet);
            setupLineChart();
            barChart.setData(barData);
            barChart.invalidate();
        }

        private void setupLineChart() {
            barChart.getAxisLeft().setEnabled(true); // Linke Y-Achse aktivieren
            barChart.getAxisRight().setEnabled(false); // Rechte Y-Achse deaktivieren

            // Nur die untere X-Achse sichtbar machen
            barChart.getXAxis().setEnabled(true); // Untere X-Achse aktivieren
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

            int white = ContextCompat.getColor(context, R.color.text_color);

            barChart.getAxisLeft().setGridColor(white); // weil wir ja oben AxisRight deaktiviert haben(recht y achse)
            barChart.getAxisLeft().setAxisLineColor(white);
            barChart.getAxisLeft().setTextColor(white);
            barChart.getAxisLeft().setAxisMinimum(0f); // Setzt den Minimalwert der Y-Achse auf 0


            barChart.getXAxis().setGridColor(white);
            barChart.getXAxis().setAxisLineColor(white);
            barChart.getXAxis().setTextColor(white);

            barChart.getDescription().setText("");

            Legend legend = barChart.getLegend();
            legend.setEnabled(false);


        }

    }

    public interface OnAddWeightClickListener {
        void onAddWeightClicked(int position);
    }
}
