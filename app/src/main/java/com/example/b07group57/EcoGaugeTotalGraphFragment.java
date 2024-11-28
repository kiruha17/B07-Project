package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;

public class EcoGaugeTotalGraphFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_eco_gauge_total_graph, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart barChart = view.findViewById(R.id.barChart);
        drawGraph(getData(barChart), barChart);
    }
    private ArrayList<BarEntry> getData(BarChart barChart) {
        // Create temp data entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 50)); // Day 1
        entries.add(new BarEntry(1, 40)); // Day 2
        entries.add(new BarEntry(2, 70)); // Day 3
        entries.add(new BarEntry(3, 30)); // Day 4
        entries.add(new BarEntry(4, 90)); // Day 5
        entries.add(new BarEntry(5, 30)); // Day 6
        entries.add(new BarEntry(6, 90)); // Day 7
        return entries;
    }
    private void drawGraph(ArrayList<BarEntry> entries, BarChart barChart) {
        // Create a BarDataSet
        BarDataSet dataSet = new BarDataSet(entries, "Weekly Stats");
        dataSet.setValueTextSize(10f); // Set text size for values
        BarData barData = new BarData(dataSet);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Minimum interval between labels
        xAxis.setGranularityEnabled(true);

// Apply data to the chart
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false); // Disable description text
        barChart.getAxisRight().setEnabled(false); // Disable right Y-axis
        barChart.animateY(1000); // Add a smooth animation
    }
}
