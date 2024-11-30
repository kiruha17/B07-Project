package com.example.b07group57;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class CalendarFragment extends Fragment {

    private DatabaseReference mDatabase;
    private PieChart pieChart;
    private TextView tvSelectedDate, tvActivityDetails;
    private Button btnDetails;

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

        // mDatabase = FirebaseDatabase.getInstance().getReference("CO2Emission");

        pieChart = view.findViewById(R.id.piechart);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvActivityDetails = view.findViewById(R.id.tvActivityDetails);
        btnDetails = view.findViewById(R.id.btnDetails);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        long currentDate = System.currentTimeMillis();

        String selectedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(currentDate));
        tvSelectedDate.setText("Selected Date: " + selectedDate);

        calendarView.setDate(currentDate, true, true);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String changedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            tvSelectedDate.setText("Selected Date: " + changedDate);
            loadEmissionData(changedDate);
        });

        loadEmissionData(selectedDate);

        btnDetails = view.findViewById(R.id.btnDetails);
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
    }


    private void loadEmissionData(String selectedDate) {
//        mDatabase.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                float transportation = 0, electricity = 0, food = 0, clothing = 0, other = 0;
//
//                if (dataSnapshot.child("Transportation").exists()) {
//                    transportation = dataSnapshot.child("Transportation").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Electricity").exists()) {
//                    electricity = dataSnapshot.child("Electricity").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Food").exists()) {
//                    food = dataSnapshot.child("Food").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Clothing").exists()) {
//                    clothing = dataSnapshot.child("Clothing").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Other").exists()) {
//                    other = dataSnapshot.child("Other").getValue(Float.class);
//                }

                float transportation = 25, electricity = 15, food = 10, clothing = 30, other = 20;

                pieChart.clearChart();
                pieChart.addPieSlice(new PieModel("Transportation", transportation, Color.parseColor("#FFA726")));
                pieChart.addPieSlice(new PieModel("Electricity", electricity, Color.parseColor("#66BB6A")));
                pieChart.addPieSlice(new PieModel("Food", food, Color.parseColor("#EF5350")));
                pieChart.addPieSlice(new PieModel("Clothing", clothing, Color.parseColor("#29B6F6")));
                pieChart.addPieSlice(new PieModel("Other", other, Color.parseColor("#FFEB3B")));

                pieChart.startAnimation();

                updateLabels(transportation, electricity, food, clothing, other);

                tvActivityDetails.setText("Activity data for " + selectedDate);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FirebaseError", "Failed: " + databaseError.getMessage());
//                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
