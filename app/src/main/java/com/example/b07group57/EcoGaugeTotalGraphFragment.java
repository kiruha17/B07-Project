package com.example.b07group57;

import static com.example.b07group57.utils.DateUtils.getDateDifference;
import static com.example.b07group57.utils.DateUtils.subtractDays;
import static com.example.b07group57.utils.DateUtils.subtractMonths;
import static com.example.b07group57.utils.DateUtils.subtractYears;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07group57.models.DailyDataLoader;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class EcoGaugeTotalGraphFragment extends Fragment {
    private String timeUnit;
    private ArrayList<Entry> entries;
    private int timeChange = 0;
    private Button previousDayButton, nextDayButton;
    private PieChart pieChart;

    private String selectedDate;
    private String[] dateWeekLabels;
    private String[] dateMonthLabels;
    private TextView tvTotal, tvDate;

    public EcoGaugeTotalGraphFragment(String timeUnit) {
        this.timeUnit = timeUnit;
    }
    private double transportation, food, clothing, energy, device, other, total = 0, finalTotal;
    private float fTransportation, fFood, fClothing, fEnergy, fDevice, fOther;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_eco_gauge_total_graph, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.piechart);
        tvTotal = view.findViewById(R.id.totalText);
        tvDate = view.findViewById(R.id.dateText);
        long currentDate = System.currentTimeMillis();
        selectedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(currentDate));
        previousDayButton = view.findViewById(R.id.leftButton);
        nextDayButton = view.findViewById(R.id.rightButton);
        entries = new ArrayList<>();
        dateWeekLabels = new String[7];
        previousDayButton.setOnClickListener(v -> {
            entries = new ArrayList<>();
            timeChange++;
            resetValue();
            switch (timeUnit) {
                case "day":
                    dayGraph(view);
                    break;
                case "week":
                    weekGraph(view);
                    break;
                case "month":
                    monthGraph(view);
                    break;
                case "year":
                    yearGraph(view);
                    break;

            }


        });

        nextDayButton.setOnClickListener(v -> {
            if (timeChange > 0) {
                timeChange--;
                resetValue();
                entries = new ArrayList<>();
                switch (timeUnit) {
                    case "day":
                        dayGraph(view);
                        break;
                    case "week":
                        weekGraph(view);
                        break;
                    case "month":
                        monthGraph(view);
                        break;
                    case "year":
                        yearGraph(view);
                        break;

                }

            }
        });

        switch (timeUnit) {
            case "day":
                entries = new ArrayList<>();
                dayGraph(view);
                break;

            case "week":
                weekGraph(view);
                break;
            case "month":
                monthGraph(view);
                break;
            case "year":
                yearGraph(view);
                break;
        }
    }
    private void drawGraph(LineChart lineChart) {
        // Create a LineDataSet
        entries.sort(Comparator.comparing(Entry::getX));
        //System.out.println("test1");
        LineDataSet dataSet = new LineDataSet(entries, selectedDate);
        dataSet.setLineWidth(2f); // Line thickness
        dataSet.setCircleRadius(4f); // Circle size at data points
        dataSet.setValueTextSize(10f); // Text size for values
        dataSet.setDrawFilled(true); // Optional: Fill the area under the line

        // Create LineData
        LineData lineData = new LineData(dataSet);

        // Configure the X-Axis
        XAxis xAxis = lineChart.getXAxis();
        //System.out.println(selectedDate + "Should be good dat");
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(dateWeekLabels));
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
    private void drawPieChart() {
        System.out.println(finalTotal);
        if (finalTotal != 0) {
            fTransportation = fTransportation / (float) finalTotal * 100;
            fFood = fFood / (float) finalTotal * 100;
            fClothing = fClothing / (float) finalTotal * 100;
            fEnergy = fEnergy / (float) finalTotal * 100;
            fDevice = fDevice / (float) finalTotal * 100;
            fOther = fOther / (float) finalTotal * 100;

            fTransportation = Math.round(fTransportation * 10) / 10.0f;
            fFood = Math.round(fFood * 10) / 10.0f;
            fClothing = Math.round(fClothing * 10) / 10.0f;
            fEnergy = Math.round(fEnergy * 10) / 10.0f;
            fDevice = Math.round(fDevice * 10) / 10.0f;
            fOther = Math.round(fOther * 10) / 10.0f;
        } else {
            fTransportation = 0;
            fFood = 0;
            fClothing = 0;
            fEnergy = 0;
            fDevice = 0;
            fOther = 0;
        }
        System.out.println(fTransportation);
        System.out.println(fFood);
        System.out.println(fClothing);
        System.out.println(fEnergy);
        System.out.println(fOther);
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Transportation", fTransportation, Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Food", fFood, Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Clothing", fClothing, Color.parseColor("#EF5350")));
        pieChart.addPieSlice(new PieModel("Energy", fEnergy, Color.parseColor("#8E24AA")));
        pieChart.addPieSlice(new PieModel("Electronic", fDevice, Color.parseColor("#42A5F5")));
        pieChart.addPieSlice(new PieModel("Other", fOther, Color.parseColor("#FFEB3B")));
        if (finalTotal == 0) {
            float fTotal = (float) finalTotal;
            pieChart.addPieSlice(new PieModel("None", fTotal, Color.parseColor("#BDBDBD")));
        }

        pieChart.startAnimation();

        updateLabels(fTransportation, fFood, fClothing, fEnergy, fDevice, fOther);
    }
    private void updateLabels(float transportation, float food, float clothing, float energy, float device, float other) {
        LinearLayout labelContainer = requireView().findViewById(R.id.labelContainer);

        labelContainer.removeAllViews();

        addLabel(labelContainer, "Transportation", transportation, Color.parseColor("#FFA726"));
        addLabel(labelContainer, "Food", food, Color.parseColor("#66BB6A"));
        addLabel(labelContainer, "Clothing", clothing, Color.parseColor("#EF5350"));
        addLabel(labelContainer, "Energy", energy, Color.parseColor("#8E24AA"));
        addLabel(labelContainer, "Electronic", device, Color.parseColor("#42A5F5"));
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

    private void resetValue() {
        transportation = 0;
        food = 0;
        clothing = 0;
        energy = 0;
        device = 0;
        other = 0;
    }
    private void allocateSavedData(String date, CalendarFragment.CalendarDataLoadedCallBack callback) {

        final int totalTasks = 3;
        final AtomicInteger completedTasks = new AtomicInteger(0);

        DailyDataLoader dailyDataLoader = new DailyDataLoader();
        dailyDataLoader.loadInputCO2Data(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> inputCO2Data) {
                // If there exists a data
                if (inputCO2Data != null) {
                    Log.d("Loaded CO2 Data", "Data: " + inputCO2Data.toString());
                    for (Map.Entry<String, Object> entry : inputCO2Data.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        double numericValue;
                        if (value instanceof Number) {
                            numericValue = ((Number) value).doubleValue();
                        } else {
                            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
                        }

                        switch (key) {
                            case "Drive":
                            case "Bus":
                            case "Train":
                            case "Subway":
                            case "ShortFlight":
                            case "LongFlight":
                                transportation += numericValue;
                                break;
                            case "Beef":
                            case "Pork":
                            case "Chicken":
                            case "Fish":
                            case "PlantBased":
                                food += numericValue;
                                break;
                            case "Clothing":
                                clothing += numericValue;
                                break;
                            case "Electricity":
                            case "Gas":
                            case "Water":
                                energy += numericValue;
                        }
                    }
                } else {
                    transportation += 0;
                    food += 0;
                    clothing += 0;
                    energy += 0;
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date);

        dailyDataLoader.loadElectronicsOrOtherCO2Data(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> electronicsCO2Data) {
                if (electronicsCO2Data != null) {
                    for (Map.Entry<String, Object> entry : electronicsCO2Data.entrySet()) {
                        Object value = entry.getValue();
                        double numericValue;
                        if (value instanceof Number) {
                            numericValue = ((Number) value).doubleValue();
                        } else {
                            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
                        }
                        device += numericValue;
                    }
                } else {
                    device += 0;
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date, "electronics");

        dailyDataLoader.loadElectronicsOrOtherCO2Data(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> OtherCO2Data) {
                if (OtherCO2Data != null) {
                    for (Map.Entry<String, Object> entry : OtherCO2Data.entrySet()) {
                        Object value = entry.getValue();
                        double numericValue;
                        if (value instanceof Number) {
                            numericValue = ((Number) value).doubleValue();
                        } else {
                            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
                        }
                        other += numericValue;
                    }
                } else {
                    other += 0;
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date, "other");
    }

    private void checkIfAllTasksCompleted(CalendarFragment.CalendarDataLoadedCallBack callback, AtomicInteger completedTasks, int totalTasks) {
        if (completedTasks.incrementAndGet() == totalTasks) {
            if (callback != null) {
                callback.onDataLoaded();
            }
        }
    }
    protected void dayGraph(View view) {
        selectedDate = subtractDays(timeChange);
        System.out.println(selectedDate);
        finalTotal = 0;
        allocateSavedData(selectedDate, new CalendarFragment.CalendarDataLoadedCallBack() {
            @Override
            public void onDataLoaded() {
                total = transportation + food + clothing + energy + device + other;
                fTransportation = (float)transportation;
                fFood = (float)food;
                fClothing = (float)clothing;
                fEnergy = (float)energy;
                fDevice = (float)device;
                fOther = (float)other;
                finalTotal += total;
                entries.add(new Entry(0, (float)total)); // Day 1
                LineChart lineChart = view.findViewById(R.id.lineChart);
                drawGraph(lineChart);
                drawPieChart();
                fTransportation = 0;
                fFood = 0;
                fClothing = 0;
                fEnergy = 0;
                fDevice = 0;
                fOther = 0;
            }
        });
        tvTotal.setText("Total: " + finalTotal);
        tvDate.setText("Date: " + selectedDate);
    }
    protected void weekGraph(View view) {
        selectedDate = subtractDays((timeChange+1)*7 - 1);
        System.out.println(selectedDate);
        finalTotal = 0;
        AtomicInteger completedCallbacks = new AtomicInteger(0);
        for (int i=0; i< 7; i++) {
            int finalI = i;
            selectedDate = subtractDays((timeChange+1)*7 - i - 1);
            dateWeekLabels[i] = selectedDate;
            //System.out.println(selectedDate);
            allocateSavedData(selectedDate, new CalendarFragment.CalendarDataLoadedCallBack() {
                @Override
                public void onDataLoaded() {
                    total = transportation + food + clothing + energy + device + other;
                    finalTotal += total;
                    fTransportation += (float)transportation;
                    fFood += (float)food;
                    fClothing += (float)clothing;
                    fEnergy += (float)energy;
                    fDevice += (float)device;
                    fOther += (float)other;
                    resetValue();
                    entries.add(new Entry(finalI, (float)total)); // Day 1
                    if (completedCallbacks.incrementAndGet() == 7) {
                        // All callbacks completed, draw the graph
                        //System.out.println(entries + "THIS IS THE ONE");
                        LineChart lineChart = view.findViewById(R.id.lineChart);
                        drawGraph(lineChart);
                        drawPieChart();
                        fTransportation = 0;
                        fFood = 0;
                        fClothing = 0;
                        fEnergy = 0;
                        fDevice = 0;
                        fOther = 0;
                        //System.out.println("Works?");
                        tvTotal.setText("Total: " + finalTotal);
                    }
                }
            });

        }

        tvDate.setText("Date: " + subtractDays((timeChange+1)*7 - 1) + " - " + selectedDate);
    }
    protected void monthGraph(View view) {
        selectedDate = subtractMonths((timeChange+1));
        int numDays = (int)getDateDifference(selectedDate, subtractMonths((timeChange)));
        int numDaysDiffNow = (int)getDateDifference(selectedDate, subtractMonths((0)));
        finalTotal = 0;
        AtomicInteger completedCallbacks = new AtomicInteger(0);
        for (int i=0; i< numDays; i++) {
            int finalI = i;

            selectedDate = subtractDays(numDaysDiffNow - i - 1);
            //System.out.println(selectedDate);
            allocateSavedData(selectedDate, new CalendarFragment.CalendarDataLoadedCallBack() {
                @Override
                public void onDataLoaded() {
                    total = transportation + food + clothing + energy + device + other;
                    finalTotal += total;
                    fTransportation += (float)transportation;
                    fFood += (float)food;
                    fClothing += (float)clothing;
                    fEnergy += (float)energy;
                    fDevice += (float)device;
                    fOther += (float)other;
                    resetValue();
                    entries.add(new Entry(finalI, (float)total)); // Day 1
                    if (completedCallbacks.incrementAndGet() == numDays) {
                        // All callbacks completed, draw the graph
                        //System.out.println(entries + "THIS IS THE ONE");
                        LineChart lineChart = view.findViewById(R.id.lineChart);
                        drawGraph(lineChart);
                        drawPieChart();
                        fTransportation = 0;
                        fFood = 0;
                        fClothing = 0;
                        fEnergy = 0;
                        fDevice = 0;
                        fOther = 0;
                        tvTotal.setText("Total: " + finalTotal);
                        //System.out.println("Works?");
                    }
                }
            });

        }

        tvDate.setText("Date: " + subtractMonths((timeChange+1)) + " - " + selectedDate);
    }
    protected void yearGraph(View view) {
        selectedDate = subtractYears((timeChange+1));
        int numDays = (int)getDateDifference(selectedDate, subtractMonths((timeChange)));
        int numDaysDiffNow = (int)getDateDifference(selectedDate, subtractMonths((0)));
        finalTotal = 0;
        AtomicInteger completedCallbacks = new AtomicInteger(0);
        for (int i=0; i< numDays; i++) {
            int finalI = i;
            selectedDate = subtractDays(numDaysDiffNow - i - 1);
            //System.out.println(selectedDate);

            allocateSavedData(selectedDate, new CalendarFragment.CalendarDataLoadedCallBack() {
                @Override
                public void onDataLoaded() {
                    total = transportation + food + clothing + energy + device + other;
                    finalTotal += total;
                    fTransportation += (float)transportation;
                    fFood += (float)food;
                    fClothing += (float)clothing;
                    fEnergy += (float)energy;
                    fDevice += (float)device;
                    fOther += (float)other;
                    resetValue();
                    entries.add(new Entry(finalI, (float)total)); // Day 1
                    if (completedCallbacks.incrementAndGet() == numDays) {
                        // All callbacks completed, draw the graph
                        //System.out.println(entries + "THIS IS THE ONE");
                        LineChart lineChart = view.findViewById(R.id.lineChart);
                        drawGraph(lineChart);
                        drawPieChart();
                        fTransportation = 0;
                        fFood = 0;
                        fClothing = 0;
                        fEnergy = 0;
                        fDevice = 0;
                        fOther = 0;
                        //System.out.println("Works?");
                        tvTotal.setText("Total: " + finalTotal);
                    }
                }
            });

        }

        tvDate.setText("Date: " + subtractYears((timeChange+1)) + " - " + selectedDate);
    }


}
