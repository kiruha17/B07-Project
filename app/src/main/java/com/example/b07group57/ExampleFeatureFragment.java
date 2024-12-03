package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExampleFeatureFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.example_feature_fragment, container, false);


        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                loadFragment(new CalendarFragment());
            } else if (item.getItemId() == R.id.gauge_nav) {
                loadFragment(new EcoGaugeFragment());
            } else if (item.getItemId() == R.id.hub_nav) {
                loadFragment(new EcoHubFragment());
            } else if (item.getItemId() == R.id.balance_nav) {
                loadFragment(new EcoBalanceFragment());
            } else if (item.getItemId() == R.id.agent_nav) {
                Toast.makeText(getContext(), "Coming soon!", Toast.LENGTH_LONG).show();
            }
            return true;

        });





        ((MainActivity) getActivity()).showNavigationBar(true);

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
