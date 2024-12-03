package com.example.b07group57;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07group57.models.DailyDataLoader;
import com.google.firebase.database.DatabaseReference;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CalendarFragment extends Fragment {

    private DatabaseReference mDatabase;
    private PieChart pieChart;
    private TextView tvSelectedDate, tvActivityDetails;
    private Button btnDetails, habit;
    private double transportation, food, clothing, energy, device, other, total = 0;
    private float fTransportation, fFood, fClothing, fEnergy, fDevice, fOther;

    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).showNavigationBar(true);
        pieChart = view.findViewById(R.id.piechart);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvActivityDetails = view.findViewById(R.id.tvActivityDetails);
        btnDetails = view.findViewById(R.id.btnDetails);
        habit = view.findViewById(R.id.habits);


        CalendarView calendarView = view.findViewById(R.id.calendarView);
        long currentDate = System.currentTimeMillis();

        String selectedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(currentDate));
        tvSelectedDate.setText("Selected Date: " + selectedDate);

        calendarView.setDate(currentDate, true, true);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String changedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            tvSelectedDate.setText("Selected Date: " + changedDate);


            allocateSavedData(changedDate, new CalendarDataLoadedCallBack() {
                @Override
                public void onDataLoaded() {
                    loadEmissionData(changedDate);
                }
            });
        });

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EcoTrackerFragment ecoTrackerFragment = new EcoTrackerFragment();
                Bundle args = new Bundle();
                args.putString("selectedDate", tvSelectedDate.getText().toString().replace("Selected Date: ", ""));
                ecoTrackerFragment.setArguments(args);
                loadFragment(ecoTrackerFragment);
            }
        });

        habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("selectedDate", tvSelectedDate.getText().toString().replace("Selected Date: ", ""));
                args.putDouble("transportation", transportation);
                args.putDouble("food", food);
                args.putDouble("clothing", clothing);
                args.putDouble("energy", energy);
                args.putDouble("device", device);
                args.putDouble("other", other);
                EcoHabitsuggestionFragment ecoHabitsuggestionFragment = new EcoHabitsuggestionFragment();
                ecoHabitsuggestionFragment.setArguments(args);
                loadFragment(ecoHabitsuggestionFragment);
            }
        });

        allocateSavedData(selectedDate, new CalendarDataLoadedCallBack() {
            @Override
            public void onDataLoaded() {
                loadEmissionData(selectedDate);
            }
        });
    }

    private void loadEmissionData(String selectedDate) {
        total = transportation + food + clothing + energy + device + other;
        if (total != 0) {
            fTransportation = (float) transportation / (float) total * 100;
            fFood = (float) food / (float) total * 100;
            fClothing = (float) clothing / (float) total * 100;
            fEnergy = (float) energy / (float) total * 100;
            fDevice = (float) device / (float) total * 100;
            fOther = (float) other / (float) total * 100;

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

        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Transportation", fTransportation, Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Food", fFood, Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Clothing", fClothing, Color.parseColor("#EF5350")));
        pieChart.addPieSlice(new PieModel("Energy", fEnergy, Color.parseColor("#8E24AA")));
        pieChart.addPieSlice(new PieModel("Electronic", fDevice, Color.parseColor("#42A5F5")));
        pieChart.addPieSlice(new PieModel("Other", fOther, Color.parseColor("#FFEB3B")));
        if (total == 0) {
            float fTotal = (float) total;
            pieChart.addPieSlice(new PieModel("None", fTotal, Color.parseColor("#BDBDBD")));
        }

        pieChart.startAnimation();

        updateLabels(fTransportation, fFood, fClothing, fEnergy, fDevice, fOther);

        tvActivityDetails.setText("Total emission of " + total + " kg");
    }

    private void updateLabels(float transportation, float food, float clothing, float energy, float device, float other) {
        LinearLayout labelContainer = getView().findViewById(R.id.labelContainer);

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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resetValue() {
        transportation = 0;
        food = 0;
        clothing = 0;
        energy = 0;
        device = 0;
        other = 0;
    }

    interface CalendarDataLoadedCallBack {
        void onDataLoaded();
    }
    // Function to get data from firebase and set default UI
    private void allocateSavedData(String date, CalendarDataLoadedCallBack callback) {
        resetValue();
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
                    transportation = 0;
                    food = 0;
                    clothing = 0;
                    energy = 0;
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
                    device = 0;
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
                    other = 0;
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date, "other");
    }

    private void checkIfAllTasksCompleted(CalendarDataLoadedCallBack callback, AtomicInteger completedTasks, int totalTasks) {
        if (completedTasks.incrementAndGet() == totalTasks) {
            if (callback != null) {
                callback.onDataLoaded();
            }
        }
    }
}

