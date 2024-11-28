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

public class TimeNavigationBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_navigation_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonToday = view.findViewById(R.id.buttonToday);
        Button buttonWeekly = view.findViewById(R.id.buttonWeekly);
        Button buttonMonthly = view.findViewById(R.id.buttonMonthly);
        Button buttonYearly = view.findViewById(R.id.buttonYearly);

        buttonToday.setOnClickListener(v -> {
            loadFragment(new EcoGaugeTodayFragment());
        });
        buttonWeekly.setOnClickListener(v -> {
            loadFragment(new EcoGaugeWeeklyFragment());
        });
        buttonMonthly.setOnClickListener(v -> {
            loadFragment(new EcoGaugeMonthlyFragment());
        });
        buttonYearly.setOnClickListener(v -> {
            loadFragment(new EcoGaugeYearlyFragment());
        });
    }
    private void loadFragment(Fragment fragment) {
        // FragmentTransaction to replace the current fragment with the new one
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Add to back stack so the user can navigate back
        transaction.commit();
    }
}


