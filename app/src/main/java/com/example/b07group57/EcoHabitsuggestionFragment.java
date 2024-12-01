package com.example.b07group57;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.b07group57.models.BasicHabitList;
import com.example.b07group57.models.HabitStructure;

import java.util.ArrayList;
import java.util.List;

public class EcoHabitsuggestionFragment extends Fragment{

    private List <HabitStructure> habits;
    private List<HabitStructure> newhabits;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habit_suggestion_fragment, container, false);

        habits = BasicHabitList.getBasicHabits(); //this is using the predefined list of habits
        newhabits = new ArrayList<>(habits);

        AutoCompleteTextView typedropdown = view.findViewById(R.id.typeDropdown);
        String[] types = {"Transportation", "Energy", "Food", "Consumption"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types);
        typedropdown.setAdapter(typeAdapter); // creation of the dropdown menu for the type

        AutoCompleteTextView impactdropdown = view.findViewById(R.id.impactDropdown);
        String[] impacts = {"High Impact", "Medium Impact", "Low Impact"};

        ArrayAdapter<String> impactAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, impacts);
        impactdropdown.setAdapter(impactAdapter); // creation of the dropdown menu for the impact

        SearchView habitsearch = view.findViewById(R.id.habitsearch);
        habitsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filter(s, typedropdown.getText().toString(), impactdropdown.getText().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s, typedropdown.getText().toString(), impactdropdown.getText().toString());
                return false;
            }
        });

        typedropdown.setOnItemClickListener((parent, view1, position, id) ->
                filter(habitsearch.getQuery().toString(), typedropdown.getText().toString(), impactdropdown.getText().toString()));

        impactdropdown.setOnItemClickListener((parent, view1, position, id) ->
                filter(habitsearch.getQuery().toString(), typedropdown.getText().toString(), impactdropdown.getText().toString()));

        return view;

    }

    private void filter(String query, String type, String impact) {
        newhabits.clear();

        for (HabitStructure habit : habits) {
            boolean newquery= habit.getDescription().toLowerCase().contains(query.toLowerCase());
            boolean newtype = habit.getType().equalsIgnoreCase(type) || type.isEmpty();
            boolean newimpact = habit.getImpact().equalsIgnoreCase(impact) || impact.isEmpty();

            if (newquery && newtype && newimpact) {
                newhabits.add(habit);
            }
        }

        if (newhabits.isEmpty()) {
            Toast.makeText(requireContext(), "No habits match your search!", Toast.LENGTH_SHORT).show();
        }


    }




}
