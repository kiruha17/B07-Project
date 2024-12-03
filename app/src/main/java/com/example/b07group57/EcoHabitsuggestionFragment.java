package com.example.b07group57;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.example.b07group57.models.BasicHabitList;
import com.example.b07group57.models.HabitStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcoHabitsuggestionFragment extends Fragment {

    private double transportation, food, clothing, energy, device, other;
    private List<HabitStructure> habits;
    private Map<String, Double> categoryMap;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habit_suggestion_fragment, container, false);

        if (getArguments() != null) {
            transportation = getArguments().getDouble("transportation", 0);
            food = getArguments().getDouble("food", 0);
            clothing = getArguments().getDouble("clothing", 0);
            energy = getArguments().getDouble("energy", 0);
            device = getArguments().getDouble("device", 0);
            other = getArguments().getDouble("other", 0);
        }

        // Initialize habit list and data
        habits = BasicHabitList.getBasicHabits(); // Predefined list of habits

        // Initialize category map
        categoryMap = new HashMap<>();
        categoryMap.put("Transportation", transportation);
        categoryMap.put("Food", food);
        categoryMap.put("Clothing", clothing);
        categoryMap.put("Energy", energy);
        categoryMap.put("Device", device);
        categoryMap.put("Other", other);

        AutoCompleteTextView typedropdown = view.findViewById(R.id.typeDropdown);
        String[] types = {"Select Category", "Transportation", "Energy", "Food", "Consumption"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types);
        typedropdown.setAdapter(typeAdapter);

        AutoCompleteTextView impactdropdown = view.findViewById(R.id.impactDropdown);
        String[] impacts = {"Select Impact", "High Impact", "Medium Impact", "Low Impact"};
        ArrayAdapter<String> impactAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, impacts);
        impactdropdown.setAdapter(impactAdapter);


        // Setup search functionality
        SearchView habitsearch = view.findViewById(R.id.habitsearch);
        habitsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query, typedropdown.getText().toString(), impactdropdown.getText().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filter(query, typedropdown.getText().toString(), impactdropdown.getText().toString());
                return false;
            }
        });

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String query = habitsearch.getQuery().toString();
            String type = typedropdown.getText().toString();
            String impact = impactdropdown.getText().toString();
            filter(query, type, impact);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initial habit suggestions
        suggestHabits();
    }

    private void suggestHabits() {
        // Sort categories by value and display the top 3
        List<Map.Entry<String, Double>> sortedCategories = new ArrayList<>(categoryMap.entrySet());
        sortedCategories.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Filter out categories that are below the average
        List<String> topCategories = new ArrayList<>();

        // Check if the total sum of category values is 0
        double totalSum = categoryMap.values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalSum == 0) {
            Toast.makeText(requireContext(), "Great job! All your categories are extremely low!", Toast.LENGTH_SHORT).show();
        }

        int count = 0;
        for (Map.Entry<String, Double> entry : sortedCategories) {
            if (entry.getValue() == 0) {
                continue;
            }
            if (count < 3) {
                topCategories.add(entry.getKey());
                count++;
            }
        }

        TextView topCategoryTextView = requireView().findViewById(R.id.topCategoryTextView);
        if (topCategories.isEmpty()) {
            String message = "All categories have extremely low emissions! \n\nYou're doing an amazing job — keep it up!";
            topCategoryTextView.setText(message);
            return;
        }

        String topCategoryMessage = "The highest CO2 emitter on this day is: " + topCategories.get(0);
        topCategoryTextView.setText(topCategoryMessage);

        // Filter habits based on top categories
        filter("", topCategories.get(0), ""); // Initial filtering to show habits based on top categories
    }

    private void filter(String query, String type, String impact) {
        List<HabitStructure> filteredHabits = new ArrayList<>();

        query = query.trim().toLowerCase();
        type = type.trim();
        impact = impact.trim();

        for (HabitStructure habit : habits) {
            boolean matchesQuery = query.isEmpty() || habit.getDescription().toLowerCase().contains(query);
            boolean matchesType = type.equals("Select Category") || type.isEmpty() || habit.getType().equalsIgnoreCase(type);
            boolean matchesImpact = impact.equals("Select Impact") || impact.isEmpty() || habit.getImpact().equalsIgnoreCase(impact);

            if (matchesQuery && matchesType && matchesImpact) {
                filteredHabits.add(habit);
            }
        }

        updateHabitListDisplay(filteredHabits);
    }


    private void updateHabitListDisplay(List<HabitStructure> filteredHabits) {
        LinearLayout habitsContainer = getView().findViewById(R.id.filteredHabitsContainer);
        habitsContainer.removeAllViews();

        Map<String, List<HabitStructure>> habitsByCategory = new HashMap<>();

        for (HabitStructure habit : filteredHabits) {
            String category = habit.getType();
            if (!habitsByCategory.containsKey(category)) {
                habitsByCategory.put(category, new ArrayList<>());
            }
            habitsByCategory.get(category).add(habit);
        }

        for (Map.Entry<String, List<HabitStructure>> entry : habitsByCategory.entrySet()) {
            String category = entry.getKey();
            List<HabitStructure> categoryHabits = entry.getValue();

            TextView categoryHeader = new TextView(requireContext());
            categoryHeader.setText(category);
            categoryHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(20);
            drawable.setColor(ContextCompat.getColor(requireContext(), R.color.primary_teal));
            categoryHeader.setBackground(drawable);
            categoryHeader.setTextSize(20);
            categoryHeader.setPadding(20, 30, 20, 20);
            habitsContainer.addView(categoryHeader);

            Map<String, List<HabitStructure>> habitsByImpact = new HashMap<>();
            for (HabitStructure habit : categoryHabits) {
                String impact = habit.getImpact();
                if (!habitsByImpact.containsKey(impact)) {
                    habitsByImpact.put(impact, new ArrayList<>());
                }
                habitsByImpact.get(impact).add(habit);
            }

            for (Map.Entry<String, List<HabitStructure>> impactEntry : habitsByImpact.entrySet()) {
                String impact = impactEntry.getKey();
                List<HabitStructure> impactHabits = impactEntry.getValue();

                TextView impactHeader = new TextView(requireContext());
                impactHeader.setText(impact);
                impactHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_teal));
                impactHeader.setTextSize(15);
                impactHeader.setPadding(20, 20, 20, 20);
                habitsContainer.addView(impactHeader);

                for (HabitStructure habit : impactHabits) {
                    CardView habitCard = new CardView(requireContext());
                    habitCard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    habitCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
                    habitCard.setCardElevation(0);
                    habitCard.setRadius(12);

                    TextView habitDescription = new TextView(requireContext());
                    habitDescription.setText(habit.getDescription());
                    habitDescription.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    habitDescription.setPadding(20, 20, 20, 20);

                    habitCard.addView(habitDescription);
                    habitsContainer.addView(habitCard);
                }
            }
        }
    }
}

