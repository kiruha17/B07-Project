package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
        LineChart lineChart = view.findViewById(R.id.lineChart);
        drawGraph(getData(lineChart), lineChart);
    }

    private ArrayList<Entry> getData(LineChart lineChart) {
        // Create temp data entries
        // Create data entries
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 50)); // Day 1
        entries.add(new Entry(1, 40)); // Day 2
        entries.add(new Entry(2, 70)); // Day 3
        entries.add(new Entry(3, 30)); // Day 4
        entries.add(new Entry(4, 90)); // Day 5
        return entries;
    }
    private void drawGraph(ArrayList<Entry> entries, LineChart lineChart) {
        // Create a LineDataSet
        LineDataSet dataSet = new LineDataSet(entries, "Weekly Stats");
        dataSet.setLineWidth(2f); // Line thickness
        dataSet.setCircleRadius(4f); // Circle size at data points
        dataSet.setValueTextSize(10f); // Text size for values
        dataSet.setDrawFilled(true); // Optional: Fill the area under the line

        // Create LineData
        LineData lineData = new LineData(dataSet);

        // Configure the X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Day 1", "Day 2", "Day 3", "Day 4", "Day 5"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Minimum interval between labels
        xAxis.setGranularityEnabled(true);

        // Apply data to the chart
        lineChart.setData(lineData);

        // Optional: Customize the chart
        lineChart.getDescription().setEnabled(false); // Disable description text
        lineChart.getAxisRight().setEnabled(false); // Disable right Y-axis
        lineChart.animateY(1000); // Add a smooth animation
    }
}
