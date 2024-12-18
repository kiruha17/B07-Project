package com.example.b07group57;

import static com.example.b07group57.utils.DateUtils.getDateDifference;
import static com.example.b07group57.utils.DateUtils.subtractDays;
import static com.example.b07group57.utils.DateUtils.subtractMonths;
import static com.example.b07group57.utils.DateUtils.subtractYears;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07group57.models.Country;
import com.example.b07group57.models.DailyDataLoader;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class EcoGaugeFragment extends Fragment {
    private String timeUnit;
    private ArrayList<Entry> entries;
    private int timeChange = 0;
    private Button previousDayButton, nextDayButton;
    private PieChart pieChart;

    private String selectedDate;
    private String[] dateWeekLabels;
    private String[] dateMonthLabels;
    private TextView tvTotal, tvDate, graphDateLeft, graphDateRight, graphDateMiddle;
    private TextView selectedItemTextView;
    private TextView selectedValueTextView;
    private List<Country> countryList;
    private ArrayAdapter<Country> adapter;

    public EcoGaugeFragment(String timeUnit) {
        this.timeUnit = timeUnit;
    }
    private double transportation, food, clothing, energy, device, other, total = 0, finalTotal;
    private float fTransportation, fFood, fClothing, fEnergy, fDevice, fOther;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_eco_gauge, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //////////////////CompareAverage/////////////////
        selectedItemTextView = view.findViewById(R.id.selectedItem);
        selectedValueTextView = view.findViewById(R.id.selectedValue);
        countryList = readCSVFromAssets();
        selectedItemTextView.setOnClickListener(v -> showSearchableDialog());

        ///////////////////////////////////////////////////

        ////////////////TimeNavigationBar////////////////////
        Button buttonToday = view.findViewById(R.id.buttonToday);
        Button buttonWeekly = view.findViewById(R.id.buttonWeekly);
        Button buttonMonthly = view.findViewById(R.id.buttonMonthly);
        Button buttonYearly = view.findViewById(R.id.buttonYearly);

        buttonToday.setOnClickListener(v -> notifyTimeUnitSelected("day"));
        buttonWeekly.setOnClickListener(v -> notifyTimeUnitSelected("week"));
        buttonMonthly.setOnClickListener(v -> notifyTimeUnitSelected("month"));
        buttonYearly.setOnClickListener(v -> notifyTimeUnitSelected("year"));
        //////////////////////////////////////////////////////

        pieChart = view.findViewById(R.id.piechart);
        ((MainActivity) getActivity()).showNavigationBar(true);
        tvTotal = view.findViewById(R.id.totalText);
        tvDate = view.findViewById(R.id.dateText);
        graphDateLeft = view.findViewById(R.id.GraphDateLeft);
        graphDateRight = view.findViewById(R.id.GraphDateRight);
        graphDateMiddle = view.findViewById(R.id.GraphDateMiddle);
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

    private void notifyTimeUnitSelected(String timeUnit) {
        this.timeUnit = timeUnit;
        resetValue();
        entries = new ArrayList<>();
        graphDateLeft.setText("");
        graphDateRight.setText("");
        graphDateMiddle.setText("");
        switch (timeUnit) {
            case "day":
                dayGraph(getView());
                break;
            case "week":
                weekGraph(getView());
                break;
            case "month":
                monthGraph(getView());
                break;
            case "year":
                yearGraph(getView());
                break;
        }
    }

    private void drawGraph(LineChart lineChart) {
        entries.sort(Comparator.comparing(Entry::getX));
        LineDataSet dataSet = new LineDataSet(entries, selectedDate);
        dataSet.setLineWidth(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(false);

        if (Objects.equals(timeUnit, "day")) {
            dataSet.setCircleRadius(8f);
            dataSet.setDrawCircles(true);
        }
        else {
            dataSet.setDrawCircles(false);
        }

        dataSet.setHighlightEnabled(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#009999"));
        dataSet.setCircleColor(Color.parseColor("#009999"));
        LineData lineData = new LineData(dataSet);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateY(1000);
        lineChart.setTouchEnabled(false);
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
        pieChart.addPieSlice(new PieModel("Transportation", fTransportation, Color.parseColor("#d8dbe2")));
        pieChart.addPieSlice(new PieModel("Food", fFood, Color.parseColor("#009999")));
        pieChart.addPieSlice(new PieModel("Clothing", fClothing, Color.parseColor("#a9bcd0")));
        pieChart.addPieSlice(new PieModel("Energy", fEnergy, Color.parseColor("#373f51")));
        pieChart.addPieSlice(new PieModel("Electronic", fDevice, Color.parseColor("#1b1b1e")));
        pieChart.addPieSlice(new PieModel("Other", fOther, Color.parseColor("#57574f")));
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
        addLabel(labelContainer, "Transportation", transportation, Color.parseColor("#d8dbe2"));
        addLabel(labelContainer, "Food", food, Color.parseColor("#009999"));
        addLabel(labelContainer, "Clothing", clothing, Color.parseColor("#a9bcd0"));
        addLabel(labelContainer, "Energy", energy, Color.parseColor("#373f51"));
        addLabel(labelContainer, "Electronic", device, Color.parseColor("#1b1b1e"));
        addLabel(labelContainer, "Other", other, Color.parseColor("#57574f"));
    }

    private void addLabel(LinearLayout container, String category, float value, int color) {
        LinearLayout labelLayout = new LinearLayout(getContext());
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams labelLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        labelLayoutParams.setMargins(0, 8, 0, 8);
        labelLayout.setLayoutParams(labelLayoutParams);


        View colorView = new View(getContext());
        LinearLayout.LayoutParams colorViewParams = new LinearLayout.LayoutParams(30, 30);
        colorViewParams.setMargins(30, 16, 16, 0);
        colorView.setLayoutParams(colorViewParams);
        colorView.setBackgroundColor(color);


        TextView textView = new TextView(getContext());
        textView.setText(category + ": " + String.format("%.1f", value) + "%");
        textView.setTextSize(16f);
        textView.setTextColor(getResources().getColor(android.R.color.black, null));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
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
                tvTotal.setText("Total: " + String.format("%.1f", finalTotal) + " kg");
            }
        });

        tvDate.setText("Date: " + selectedDate);
        graphDateMiddle.setText(selectedDate);
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
                        tvTotal.setText("Total: " + String.format("%.1f", finalTotal) + " kg");
                    }
                }
            });

        }

        tvDate.setText("Date: " + subtractDays((timeChange+1)*7 - 1) + " - " + selectedDate);
        graphDateLeft.setText(subtractDays((timeChange+1)*7 - 1));
        graphDateRight.setText(selectedDate);
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
                        tvTotal.setText("Total: " + String.format("%.1f", finalTotal) + " kg");
                        //System.out.println("Works?");
                    }
                }
            });

        }

        tvDate.setText("Date: " + subtractMonths((timeChange+1)) + " - " + selectedDate);
        graphDateLeft.setText(subtractMonths((timeChange+1)));
        graphDateRight.setText(selectedDate);
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
                        tvTotal.setText("Total: " + String.format("%.1f", finalTotal) + " kg");
                    }
                }
            });
        }

        tvDate.setText("Date: " + subtractYears((timeChange+1)) + " - " + selectedDate);
        graphDateLeft.setText(subtractYears((timeChange+1)));
        graphDateRight.setText(selectedDate);
    }

    private List<Country> readCSVFromAssets() {
        List<Country> countries = new ArrayList<>();
        try {
            InputStream inputStream = requireContext().getAssets().open("Global_Averages.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String countryName = parts[0].trim();
                    String countryValue = parts[1].trim();
                    countries.add(new Country(countryName, countryValue));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }

    private void showSearchableDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_searchable_list);

        EditText searchEditText = dialog.findViewById(R.id.searchEditText);
        ListView countryListView = dialog.findViewById(R.id.countryListView);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, countryList);
        countryListView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        countryListView.setOnItemClickListener((parent, view, position, id) -> {
            Country selectedCountry = adapter.getItem(position);
            if (selectedCountry != null) {
                selectedItemTextView.setText(selectedCountry.getName());
                double averageTotal = 0;
                switch (timeUnit) {

                    case "day":
                        averageTotal = (Double.parseDouble(selectedCountry.getValue()) * 907.2) / 365;
                        break;
                    case "week":
                        averageTotal = (Double.parseDouble(selectedCountry.getValue()) * 907.2) / 54;
                        break;
                    case "month":
                        averageTotal = (Double.parseDouble(selectedCountry.getValue()) * 907.2) / 30;
                        break;
                    case "year":
                        averageTotal = Double.parseDouble(selectedCountry.getValue()) * 907.2;
                        break;

                }
                selectedValueTextView.setText("Average country emissions this " + timeUnit + ": " + String.format("%.1f", averageTotal) +
                        " kg\nYour emissions this " + timeUnit + ": " + String.format("%.1f", finalTotal) + " kg\nYou Are " +
                        String.format("%.1f", (finalTotal*100/averageTotal)) + "% of average");
            }
            dialog.dismiss();
        });
        dialog.show();
    }
}
