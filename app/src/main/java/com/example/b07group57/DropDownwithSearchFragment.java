package com.example.b07group57;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07group57.models.Country;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DropDownwithSearchFragment extends Fragment {
    private TextView selectedItemTextView;
    private TextView selectedValueTextView;
    private List<Country> countryList;
    private ArrayAdapter<Country> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropdown_selector, container, false);

        // Initialize views
        selectedItemTextView = view.findViewById(R.id.selectedItem);
        selectedValueTextView = view.findViewById(R.id.selectedValue);

        // Populate the list of countries
        countryList = readCSVFromAssets();

        // Set onClickListener to show the dialog
        selectedItemTextView.setOnClickListener(v -> showSearchableDialog());

        return view;
    }

    // Populate country list with names and values
    private List<Country> readCSVFromAssets() {
        List<Country> countries = new ArrayList<>();
        try {
            // Open the CSV file from the assets folder
            InputStream inputStream = requireContext().getAssets().open("Global_Averages.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by commas
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String countryName = parts[0].trim();
                    String countryValue = parts[1].trim();
                    countries.add(new Country(countryName, countryValue));
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }

    // Show a searchable dialog
    private void showSearchableDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_searchable_list);

        // Initialize dialog views
        EditText searchEditText = dialog.findViewById(R.id.searchEditText);
        ListView countryListView = dialog.findViewById(R.id.countryListView);

        // Set up the adapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, countryList);
        countryListView.setAdapter(adapter);

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // Filter the adapter based on input
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set item click listener to update the selected item and value
        countryListView.setOnItemClickListener((parent, view, position, id) -> {
            Country selectedCountry = adapter.getItem(position);
            if (selectedCountry != null) {
                selectedItemTextView.setText(selectedCountry.getName()); // Update the country name
                selectedValueTextView.setText(selectedCountry.getValue()); // Show the associated value
            }
            dialog.dismiss(); // Close the dialog
        });

        dialog.show(); // Display the dialog
    }
}
