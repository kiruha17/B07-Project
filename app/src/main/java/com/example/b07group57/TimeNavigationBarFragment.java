package com.example.b07group57;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimeNavigationBarFragment extends Fragment {

    // Interface to communicate with parent fragment
    public interface OnTimeUnitSelectedListener {
        void onTimeUnitSelected(String timeUnit);
    }

    private OnTimeUnitSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_navigation_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find buttons by their IDs
        Button buttonToday = view.findViewById(R.id.buttonToday);
        Button buttonWeekly = view.findViewById(R.id.buttonWeekly);
        Button buttonMonthly = view.findViewById(R.id.buttonMonthly);
        Button buttonYearly = view.findViewById(R.id.buttonYearly);

        // Set click listeners for buttons
        buttonToday.setOnClickListener(v -> notifyTimeUnitSelected("day"));
        buttonWeekly.setOnClickListener(v -> notifyTimeUnitSelected("week"));
        buttonMonthly.setOnClickListener(v -> notifyTimeUnitSelected("month"));
        buttonYearly.setOnClickListener(v -> notifyTimeUnitSelected("year"));
    }

    // Set the listener from the parent fragment
    public void setOnTimeUnitSelectedListener(OnTimeUnitSelectedListener listener) {
        this.listener = listener;
    }

    // Notify the parent fragment of the selected time unit
    private void notifyTimeUnitSelected(String timeUnit) {
        if (listener != null) {
            listener.onTimeUnitSelected(timeUnit);
        }
    }
}


