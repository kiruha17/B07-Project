package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EcoGaugeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eco_gauge, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Dynamically add the GraphFragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.timeNavBar, new TimeNavigationBarFragment()) // Replace placeholder with GraphFragment
                .commit();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.Graph, new EcoGaugeTotalGraphFragment()) // Replace placeholder with GraphFragment
                .commit();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.pieChart, new BreakdownPieChart()) // Replace placeholder with GraphFragment
                .commit();

    }
}
