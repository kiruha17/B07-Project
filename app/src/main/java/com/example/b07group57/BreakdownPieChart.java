package com.example.b07group57;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class BreakdownPieChart extends Fragment {
    private PieChart pieChart;

    private TextView tvActivityDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_breakdown_pie_chart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.piechart);

        float transportation = 25, electricity = 15, food = 10, clothing = 30, other = 20;

        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Transportation", transportation, Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Electricity", electricity, Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Food", food, Color.parseColor("#EF5350")));
        pieChart.addPieSlice(new PieModel("Clothing", clothing, Color.parseColor("#29B6F6")));
        pieChart.addPieSlice(new PieModel("Other", other, Color.parseColor("#FFEB3B")));

        pieChart.startAnimation();
        updateLabels(transportation, electricity, food, clothing, other);

    }
    private void updateLabels(float transportation, float electricity, float food, float clothing, float other) {
        LinearLayout labelContainer = getView().findViewById(R.id.labelContainer);

        labelContainer.removeAllViews();

        addLabel(labelContainer, "Transportation", transportation, Color.parseColor("#FFA726"));
        addLabel(labelContainer, "Electricity", electricity, Color.parseColor("#66BB6A"));
        addLabel(labelContainer, "Food", food, Color.parseColor("#EF5350"));
        addLabel(labelContainer, "Clothing", clothing, Color.parseColor("#29B6F6"));
        addLabel(labelContainer, "Other", other, Color.parseColor("#FFEB3B"));
    }
    private void addLabel(LinearLayout container, String category, float value, int color) {
        LinearLayout labelLayout = new LinearLayout(getContext());
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);
        labelLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        View colorView = new View(getContext());
        colorView.setLayoutParams(new LinearLayout.LayoutParams(15, 15));
        colorView.setBackgroundColor(color);

        TextView textView = new TextView(getContext());
        textView.setText(category + ": " + value + "%");
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding(10, 0, 0, 0);

        labelLayout.addView(colorView);
        labelLayout.addView(textView);
        container.addView(labelLayout);
    }
}
