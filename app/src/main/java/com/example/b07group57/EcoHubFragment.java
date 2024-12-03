package com.example.b07group57;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EcoHubFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eco_hub_fragment, container, false);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                loadFragment(new CalendarFragment());
            } else if (item.getItemId() == R.id.gauge_nav) {
                loadFragment(new ExampleFeatureFragment());
            } else if (item.getItemId() == R.id.hub_nav) {
                loadFragment(new EcoHubFragment());
            } else if (item.getItemId() == R.id.balance_nav) {
                loadFragment(new ExampleFeatureFragment());
            } else if (item.getItemId() == R.id.agent_nav) {
                loadFragment(new ExampleFeatureFragment());
            }
            return true;

        });
        // Link each button to its URL
        setupButton(view.findViewById(R.id.sustainabilityBasicsButton), "https://planetze.io/");
        setupButton(view.findViewById(R.id.climateChangeButton), "https://planetze.io/");
        setupButton(view.findViewById(R.id.ecoLivingButton), "https://planetze.io/");
        setupButton(view.findViewById(R.id.greenTechButton), "https://planetze.io/");
        setupButton(view.findViewById(R.id.ecoProductsButton), "https://planetze.io/");
        setupButton(view.findViewById(R.id.sustainableFashionButton), "https://planetze.io/");

        return view;
    }

    private void setupButton(Button button, String url) {
        button.setOnClickListener(v -> openLink(url));
    }

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}