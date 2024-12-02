package com.example.b07group57;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07group57.MainMenuFragment;
import com.example.b07group57.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ACFDisplayPageFragment extends Fragment {

    private static final double GLOBAL_TARGET = 2.0; // Global target in metric tons CO2 per year

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acf_display_page_fragment, container, false);

        TextView totalTextView = view.findViewById(R.id.total_text_view);
        TextView transportationTextView = view.findViewById(R.id.transportation_text_view);
        TextView foodTextView = view.findViewById(R.id.food_text_view);
        TextView housingTextView = view.findViewById(R.id.housing_text_view);
        TextView consumptionTextView = view.findViewById(R.id.consumption_text_view);
        TextView comparisonTextView = view.findViewById(R.id.comparison_text_view);
        TextView globalComparisonTextView = view.findViewById(R.id.global_comparison_text_view);
        Button nextButton = view.findViewById(R.id.next_button);

        Bundle args = getArguments();
        if (args != null) {
            double total = args.getDouble("total", -1);
            double transportation = args.getDouble("transportation", -1);
            double food = args.getDouble("food", -1);
            double housing = args.getDouble("housing", -1);
            double consumption = args.getDouble("consumption", -1);
            String userCountry = args.getString("country", "World");

            // Display the breakdown
            totalTextView.setText(String.format("Total Carbon Footprint: %.2f metric tons of CO2", total * 0.001));
            transportationTextView.setText(String.format("Transportation: %.2f kg CO2", transportation));
            foodTextView.setText(String.format("Food: %.2f kg CO2", food));
            housingTextView.setText(String.format("Housing: %.2f kg CO2", housing));
            consumptionTextView.setText(String.format("Consumption: %.2f kg CO2", consumption));

            // Country comparison
            try {
                String jsonString = JsonUtils.loadJSONFromAsset(requireContext(), "co2_per_capitas.json");
                JSONObject jsonObject = new JSONObject(jsonString);
                double countryAverage = jsonObject.optDouble(userCountry, 0);

                if (countryAverage > 0) {
                    double percentageDifference = ((total * 0.001) - countryAverage) / countryAverage * 100;
                    if (percentageDifference > 0) {
                        comparisonTextView.setText(String.format("You are %.2f%% above your country's average (%.2f metric tons).", percentageDifference, countryAverage));
                    } else {
                        comparisonTextView.setText(String.format("You are %.2f%% below your country's average (%.2f metric tons).", Math.abs(percentageDifference), countryAverage));
                    }
                } else {
                    comparisonTextView.setText("Country average data not available.");
                }

            } catch (JSONException e) {
                comparisonTextView.setText("Error loading country data.");
                e.printStackTrace();
            }

            // Global target comparison
            double globalPercentageDifference = ((total * 0.001) - GLOBAL_TARGET) / GLOBAL_TARGET * 100;
            if (globalPercentageDifference > 0) {
                globalComparisonTextView.setText(String.format("You are %.2f%% above the global target (%.2f metric tons).", globalPercentageDifference, GLOBAL_TARGET));
            } else {
                globalComparisonTextView.setText(String.format("You are %.2f%% below the global target (%.2f metric tons).", Math.abs(globalPercentageDifference), GLOBAL_TARGET));
            }
        }

        nextButton.setOnClickListener(v -> {
            Fragment mainMenuFragment = new MainMenuFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainMenuFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}