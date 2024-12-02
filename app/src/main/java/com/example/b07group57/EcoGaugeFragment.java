package com.example.b07group57;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07group57.utils.EcoGaugeCalculator;


public class EcoGaugeFragment extends Fragment implements TimeNavigationBarFragment.OnTimeUnitSelectedListener {

    private String timeUnit;
    private long lastTimeUnitSelectionTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eco_gauge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Default time unit
        timeUnit = "month";

        // Add TimeNavigationBarFragment
        TimeNavigationBarFragment timeNavBarFragment = new TimeNavigationBarFragment();
        timeNavBarFragment.setOnTimeUnitSelectedListener(this);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.timeNavBar, timeNavBarFragment)
                .commit();

        // Add GraphFragment with default time unit
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.Graph, new EcoGaugeTotalGraphFragment(timeUnit))
                .commit();
/*
        // Add BreakdownPieChart
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.pieChart, new BreakdownPieChart(timeUnit))
                .commit();

 */
    }

    // Callback method when a time unit is selected
    @Override
    public void onTimeUnitSelected(String timeUnit) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeUnitSelectionTime < 500) {
            return; // Debounce rapid clicks
        }
        lastTimeUnitSelectionTime = currentTime;

        if (!this.timeUnit.equals(timeUnit)) {
            this.timeUnit = timeUnit;
            Log.d("EcoGaugeFragment", "Time Unit Selected: " + timeUnit);

            getChildFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.Graph, new EcoGaugeTotalGraphFragment(timeUnit))
                    .commit();
        }
    }


}