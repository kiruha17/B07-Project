package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
public class MainMenuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);

        RelativeLayout ecoTrackerCard = view.findViewById(R.id.ecotrackercard);
        RelativeLayout ecoGaugeCard = view.findViewById(R.id.ecogaugecard);
        RelativeLayout ecoHubCard = view.findViewById(R.id.ecohubcard);
        RelativeLayout ecoBalanceCard = view.findViewById(R.id.ecobalancecard);
        RelativeLayout ecoAgentCard = view.findViewById(R.id.ecoagentcard);

        ecoTrackerCard.setOnClickListener(v -> {
            loadFragment(new EcoTrackerFragment());
        });

        ecoGaugeCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Eco Gauge clicked", Toast.LENGTH_SHORT).show();
        });

        ecoHubCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Eco Hub clicked", Toast.LENGTH_SHORT).show();
        });

        ecoBalanceCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Eco Balance clicked", Toast.LENGTH_SHORT).show();
        });

        ecoAgentCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Eco Agent clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
