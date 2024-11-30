package com.example.b07group57;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import android.widget.Toast;

import com.example.b07group57.models.EcoTrackerEmissionsCalculator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EcoTrackerFragment extends Fragment {
    private String selectedDate;
    private EditText driveInput, cyclingWalkingInput, busInput, trainInput, subwayInput, shortFlightInput,
            longFlightInput, beefInput, porkInput, chickenInput, fishInput, plantBasedInput,
            clothingInput, electricityBillsInput, gasBillsInput, waterBillsInput;
    private Button btnEdit, addElectronicDeviceButton, addOtherButton;
    private Spinner fuelTypeSpinner;
    private boolean isEditable = false;
    private List<TextView> deleteTextList = new ArrayList<>();
    private List<TextView> inputTypeTextList = new ArrayList<>();
    private List<TextView> inputTextList = new ArrayList<>();
    private List<LinearLayout> electronicsInputs = new ArrayList<>();
    private List<LinearLayout> otherInputs = new ArrayList<>();

    public EcoTrackerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eco_tracker_fragment, container, false);

        // Set up the TextView for each category title
        LinearLayout transportationSection = view.findViewById(R.id.transportationSection);
        LinearLayout foodSection = view.findViewById(R.id.foodSection);
        LinearLayout shoppingSection = view.findViewById(R.id.shoppingSection);

        // Set up the LinearLayouts that will hold the input fields for each category
        LinearLayout transportationField = view.findViewById(R.id.transportationInputFields);
        LinearLayout foodField = view.findViewById(R.id.foodInputFields);
        LinearLayout shoppingField = view.findViewById(R.id.shoppingInputFields);

        // Handle the NestedScrollView
        NestedScrollView scrollView = view.findViewById(R.id.nestedScrollView);

        driveInput = view.findViewById(R.id.driveInput);
        cyclingWalkingInput = view.findViewById(R.id.cyclingWalkingInput);
        busInput = view.findViewById(R.id.busInput);
        trainInput = view.findViewById(R.id.trainInput);
        subwayInput = view.findViewById(R.id.subwayInput);
        shortFlightInput = view.findViewById(R.id.shortFlightInput);
        longFlightInput = view.findViewById(R.id.longFlightInput);
        beefInput = view.findViewById(R.id.beefInput);
        porkInput = view.findViewById(R.id.porkInput);
        chickenInput = view.findViewById(R.id.chickenInput);
        fishInput = view.findViewById(R.id.fishInput);
        plantBasedInput = view.findViewById(R.id.plantBasedInput);
        clothingInput = view.findViewById(R.id.clothingInput);
        electricityBillsInput = view.findViewById(R.id.electricityBillsInput);
        gasBillsInput = view.findViewById(R.id.gasBillsInput);
        waterBillsInput = view.findViewById(R.id.waterBillsInput);


        // Set up listeners for category titles to toggle visibility of sections
        transportationSection.setOnClickListener(v -> {
            toggleVisibility(transportationField);
            scrollToView(scrollView, transportationField);  // Scroll to the section when it becomes visible
        });
        foodSection.setOnClickListener(v -> {
            toggleVisibility(foodField);
            scrollToView(scrollView, foodField);  // Scroll to the section when it becomes visible
        });
        shoppingSection.setOnClickListener(v -> {
            toggleVisibility(shoppingField);
            scrollToView(scrollView, shoppingField);  // Scroll to the section when it becomes visible
        });

        btnEdit = view.findViewById(R.id.btnEdit);
        addElectronicDeviceButton = view.findViewById(R.id.addElectronicDeviceButton);
        addOtherButton = view.findViewById(R.id.addOtherButton);

        // Set up listeners for "Add Field" buttons
        LinearLayout devicePairs = view.findViewById(R.id.devicePairs);
        LinearLayout otherPairs = view.findViewById(R.id.otherPairs);
        addElectronicDeviceButton.setOnClickListener(v -> addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs, "electronics"));
        addOtherButton.setOnClickListener(v -> addNewInput("type (e.g. furniture, appliances)", otherPairs, "other"));

        btnEdit.setOnClickListener(v -> {
            if (isEditable) {
                calculateEmissions();
                savetoDB();
                enableEditText(false);
                setSpinnerEditable(false);
                btnEdit.setText("Edit");
                setAddDeleteButtonsEnabled(false);
            } else {
                enableEditText(true);
                setSpinnerEditable(true);
                btnEdit.setText("Save");
                setAddDeleteButtonsEnabled(true);
            }
            isEditable = !isEditable;
        });

        // default settings
        fuelTypeSpinner = view.findViewById(R.id.fuelTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.fuel_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(adapter);
        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedFuelType = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when no item is selected
            }
        });
        addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs, "electronics");
        addNewInput("type (e.g. furniture, appliances)", otherPairs, "other");
        setAddDeleteButtonsEnabled(isEditable);
        enableEditText(isEditable);
        setSpinnerEditable(isEditable);

        //Grabs the current date the user has selected
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate", "");
        }

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                loadFragment(new EcoTrackerFragment());
            } else if (item.getItemId() == R.id.gauge_nav) {
                loadFragment(new ExampleGaugeFragment());
            } else if (item.getItemId() == R.id.hub_nav) {
                loadFragment(new EcoHubFragment());
            } else if (item.getItemId() == R.id.balance_nav) {
                loadFragment(new ExampleGaugeFragment());
            } else if (item.getItemId() == R.id.agent_nav) {
                loadFragment(new ExampleGaugeFragment());
            }
            return true;

        });

        return view;
    }


    private void setSpinnerEditable(boolean isEnabled) {
        fuelTypeSpinner.setEnabled(isEnabled);  // Enable or disable spinner based on isEditable state
    }

    // Toggle visibility of a section
    private void toggleVisibility(LinearLayout section) {
        if (section.getVisibility() == View.VISIBLE) {
            section.setVisibility(View.GONE);
        } else {
            section.setVisibility(View.VISIBLE);
        }
    }

    private void addNewInput(String type, LinearLayout parent, String category) {
        LinearLayout newPair = new LinearLayout(getContext());
        newPair.setOrientation(LinearLayout.VERTICAL);

        EditText newTypeInput = new EditText(getContext());
        newTypeInput.setHint(type);
        EditText newInput = new EditText(getContext());
        newInput.setHint("(quantity)");

        TextView deleteText = createDeleteText(getContext(), newPair);

        newPair.addView(deleteText);
        newPair.addView(newTypeInput);
        inputTypeTextList.add(newTypeInput);
        newPair.addView(newInput);
        inputTextList.add(newInput);

        if (newPair.getParent() != null) {
            ((ViewGroup) newPair.getParent()).removeView(newPair);
        }

        parent.addView(newPair);

        // Store the input data in the appropriate category
        if ("electronics".equals(category)) {
            electronicsInputs.add(newPair);  // Store in electronics inputs list
        } else if ("other".equals(category)) {
            otherInputs.add(newPair);  // Store in other inputs list
        }
    }

    public TextView createDeleteText(Context context, ViewGroup parentLayout) {
        TextView deleteText = new TextView(context);
        deleteText.setText("x");
        deleteText.setTextSize(20);
        deleteText.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        deleteText.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 8);
        layoutParams.gravity = Gravity.END;

        deleteText.setLayoutParams(layoutParams);
        deleteText.setClickable(true);
        deleteText.setOnClickListener(v -> {
            deleteTextList.remove(deleteText);
            deletePair(parentLayout);
        });
        deleteTextList.add(deleteText);

        return deleteText;
    }

    public void deletePair(ViewGroup parentLayout) {
        if (parentLayout != null && parentLayout.getParent() instanceof ViewGroup) {
            ViewGroup grandParentLayout = (ViewGroup) parentLayout.getParent();
            grandParentLayout.removeView(parentLayout);
        }
    }

    // Scroll to the given view in the NestedScrollView
    private void scrollToView(NestedScrollView scrollView, View view) {
        scrollView.post(() -> {
            // Scroll to the view within the scroll view, ensuring it stays visible within the viewport
            scrollView.smoothScrollTo(0, view.getTop());
        });
    }

    private void enableEditText(boolean isEnabled) {
        driveInput.setFocusable(isEnabled);
        driveInput.setFocusableInTouchMode(isEnabled);
        cyclingWalkingInput.setFocusable(isEnabled);
        cyclingWalkingInput.setFocusableInTouchMode(isEnabled);
        busInput.setFocusable(isEnabled);
        busInput.setFocusableInTouchMode(isEnabled);
        trainInput.setFocusable(isEnabled);
        trainInput.setFocusableInTouchMode(isEnabled);
        subwayInput.setFocusable(isEnabled);
        subwayInput.setFocusableInTouchMode(isEnabled);
        shortFlightInput.setFocusable(isEnabled);
        shortFlightInput.setFocusableInTouchMode(isEnabled);
        longFlightInput.setFocusable(isEnabled);
        longFlightInput.setFocusableInTouchMode(isEnabled);
        beefInput.setFocusable(isEnabled);
        beefInput.setFocusableInTouchMode(isEnabled);
        porkInput.setFocusable(isEnabled);
        porkInput.setFocusableInTouchMode(isEnabled);
        chickenInput.setFocusable(isEnabled);
        chickenInput.setFocusableInTouchMode(isEnabled);
        fishInput.setFocusable(isEnabled);
        fishInput.setFocusableInTouchMode(isEnabled);
        plantBasedInput.setFocusable(isEnabled);
        plantBasedInput.setFocusableInTouchMode(isEnabled);
        clothingInput.setFocusable(isEnabled);
        clothingInput.setFocusableInTouchMode(isEnabled);
        electricityBillsInput.setFocusable(isEnabled);
        electricityBillsInput.setFocusableInTouchMode(isEnabled);
        gasBillsInput.setFocusable(isEnabled);
        gasBillsInput.setFocusableInTouchMode(isEnabled);
        waterBillsInput.setFocusable(isEnabled);
        waterBillsInput.setFocusableInTouchMode(isEnabled);
        for (TextView inputTypeText : inputTypeTextList) {
            inputTypeText.setFocusable(isEnabled);
            inputTypeText.setFocusableInTouchMode(isEnabled);
        }
        for (TextView inputText : inputTextList) {
            inputText.setFocusable(isEnabled);
            inputText.setFocusableInTouchMode(isEnabled);
        }
    }

    private void savetoDB() {
        // Collect data from all inputs
        HashMap<String, Object> inputData = new HashMap<>();
        inputData.put("Drive", getDoubleFromEditText(driveInput));
        inputData.put("CyclingWalking", getDoubleFromEditText(cyclingWalkingInput));
        inputData.put("Bus", getDoubleFromEditText(busInput));
        inputData.put("Train", getDoubleFromEditText(trainInput));
        inputData.put("Subway", getDoubleFromEditText(subwayInput));
        inputData.put("ShortFlight", getDoubleFromEditText(shortFlightInput));
        inputData.put("LongFlight", getDoubleFromEditText(longFlightInput));
        inputData.put("Beef", getDoubleFromEditText(beefInput));
        inputData.put("Pork", getDoubleFromEditText(porkInput));
        inputData.put("Chicken", getDoubleFromEditText(chickenInput));
        inputData.put("Fish", getDoubleFromEditText(fishInput));
        inputData.put("PlantBased", getDoubleFromEditText(plantBasedInput));
        inputData.put("Clothing", getDoubleFromEditText(clothingInput));
        inputData.put("Electricity", getDoubleFromEditText(electricityBillsInput));
        inputData.put("Gas", getDoubleFromEditText(gasBillsInput));
        inputData.put("Water", getDoubleFromEditText(waterBillsInput));

        HashMap<String, Object> electronicsData = new HashMap<>();
        for (int i = 0; i < electronicsInputs.size(); i++) {
            EditText typeInput = (EditText) electronicsInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) electronicsInputs.get(i).getChildAt(2);  // Get quantity input
            electronicsData.put(typeInput.getText().toString(), getDoubleFromEditText(quantityInput));
        }

        // Collect Other Inputs
        HashMap<String, Object> otherData = new HashMap<>();
        for (int i = 0; i < otherInputs.size(); i++) {
            EditText typeInput = (EditText) otherInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) otherInputs.get(i).getChildAt(2);  // Get quantity input
            otherData.put(typeInput.getText().toString(), getDoubleFromEditText(quantityInput));
        }

        // Combine all inputs
        inputData.putAll(electronicsData);
        inputData.putAll(otherData);

        //Calculate individual CO2e
        HashMap<String, Object> co2eData = new HashMap<>();
        co2eData.put("Drive", EcoTrackerEmissionsCalculator.calculateEmissions("Drive Personal Vehicle", getDoubleFromEditText(driveInput), "Gasoline"));
        co2eData.put("CyclingWalking", EcoTrackerEmissionsCalculator.calculateEmissions("Cycling or Walking", getDoubleFromEditText(cyclingWalkingInput), ""));
        co2eData.put("Bus", EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", getDoubleFromEditText(busInput), "Bus"));
        co2eData.put("Train", EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", getDoubleFromEditText(trainInput), "Train"));
        co2eData.put("Subway", EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", getDoubleFromEditText(subwayInput), "Subway"));
        co2eData.put("ShortFlight", EcoTrackerEmissionsCalculator.calculateEmissions("Flight", getDoubleFromEditText(shortFlightInput), "Short-Haul"));
        co2eData.put("LongFlight", EcoTrackerEmissionsCalculator.calculateEmissions("Flight", getDoubleFromEditText(longFlightInput), "Long-Haul"));
        co2eData.put("Beef", EcoTrackerEmissionsCalculator.calculateEmissions("Meal", getDoubleFromEditText(beefInput), "Beef"));
        co2eData.put("Pork", EcoTrackerEmissionsCalculator.calculateEmissions("Meal", getDoubleFromEditText(porkInput), "Pork"));
        co2eData.put("Chicken", EcoTrackerEmissionsCalculator.calculateEmissions("Meal", getDoubleFromEditText(chickenInput), "Chicken"));
        co2eData.put("Fish", EcoTrackerEmissionsCalculator.calculateEmissions("Meal", getDoubleFromEditText(fishInput), "Fish"));
        co2eData.put("PlantBased", EcoTrackerEmissionsCalculator.calculateEmissions("Meal", getDoubleFromEditText(plantBasedInput), "Plant-based"));
        co2eData.put("Clothing", EcoTrackerEmissionsCalculator.calculateEmissions("Buy New Clothes", getDoubleFromEditText(clothingInput), ""));
        co2eData.put("Electricity", EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", getDoubleFromEditText(electricityBillsInput), "Electricity"));
        co2eData.put("Gas", EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", getDoubleFromEditText(gasBillsInput), "Gas"));
        co2eData.put("Water", EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", getDoubleFromEditText(waterBillsInput), "Water"));

        HashMap<String, Object> electronicsDataCO2 = new HashMap<>();
        for (int i = 0; i < electronicsInputs.size(); i++) {
            EditText typeInput = (EditText) electronicsInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) electronicsInputs.get(i).getChildAt(2);  // Get quantity input
            double emissions = EcoTrackerEmissionsCalculator.calculateEmissions("Buy Electronics", getDoubleFromEditText(quantityInput), "");
            electronicsDataCO2.put(typeInput.getText().toString(), emissions);
        }

        // Collect Other Inputs
        HashMap<String, Object> otherDataCO2 = new HashMap<>();
        for (int i = 0; i < otherInputs.size(); i++) {
            EditText typeInput = (EditText) otherInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) otherInputs.get(i).getChildAt(2);  // Get quantity input
            double emissions = EcoTrackerEmissionsCalculator.calculateEmissions("Other Purchases", getDoubleFromEditText(quantityInput), "");
            otherDataCO2.put(typeInput.getText().toString(), emissions);
        }


        EcoTrackerFragmentModel m = new EcoTrackerFragmentModel();
        m.saveDailyInputDB(inputData, electronicsData, otherData, co2eData, electronicsDataCO2, otherDataCO2, selectedDate);
    }
    private void setAddDeleteButtonsEnabled(boolean isEnabled) {
        for (TextView deleteText : deleteTextList) {
            deleteText.setClickable(isEnabled);
            deleteText.setFocusable(isEnabled);
            deleteText.setTextColor(isEnabled
                    ? getResources().getColor(android.R.color.holo_red_dark)
                    : getResources().getColor(android.R.color.darker_gray));
        }

        addElectronicDeviceButton.setEnabled(isEnabled);
        addOtherButton.setEnabled(isEnabled);
    }

    // Calculate emissions (Thanks to Colten)
    private void calculateEmissions() {
        double totalEmissions = 0.0;

        // Gather the user inputs for each category
        double driveAmount = getDoubleFromEditText(driveInput);
        double cyclingWalkingAmount = getDoubleFromEditText(cyclingWalkingInput);
        double busAmount = getDoubleFromEditText(busInput);
        double trainAmount = getDoubleFromEditText(trainInput);
        double subwayAmount = getDoubleFromEditText(subwayInput);
        double shortFlightAmount = getDoubleFromEditText(shortFlightInput);
        double longFlightAmount = getDoubleFromEditText(longFlightInput);
        double beefAmount = getDoubleFromEditText(beefInput);
        double porkAmount = getDoubleFromEditText(porkInput);
        double chickenAmount = getDoubleFromEditText(chickenInput);
        double fishAmount = getDoubleFromEditText(fishInput);
        double plantBasedAmount = getDoubleFromEditText(plantBasedInput);
        double clothingAmount = getDoubleFromEditText(clothingInput);
        double electricityBillsAmount = getDoubleFromEditText(electricityBillsInput);
        double gasBillsAmount = getDoubleFromEditText(gasBillsInput);
        double waterBillsAmount = getDoubleFromEditText(waterBillsInput);

        // Get emissions for each category
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Drive Personal Vehicle", driveAmount, "Gasoline");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Cycling or Walking", cyclingWalkingAmount, "");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", busAmount, "Bus");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", trainAmount, "Train");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Take Public Transportation", subwayAmount, "Subway");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Flight", shortFlightAmount, "Short-Haul");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Flight", longFlightAmount, "Long-Haul");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Meal", beefAmount, "Beef");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Meal", porkAmount, "Pork");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Meal", chickenAmount, "Chicken");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Meal", fishAmount, "Fish");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Meal", plantBasedAmount, "Plant-based");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Buy New Clothes", clothingAmount, "");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", electricityBillsAmount, "Electricity");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", gasBillsAmount, "Gas");
        totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Energy Bills", waterBillsAmount, "Water");

        // Update the result TextView
        TextView emissionsResultText = getView().findViewById(R.id.emissionsResultText);
        emissionsResultText.setText(String.format("Emissions: %.2f kg CO2e", totalEmissions));
    }

    // Helper method to safely get a double from the EditText
    private double getDoubleFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                // Show a Toast message or set error on the EditText when input is invalid
                Toast.makeText(getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                // Optionally set an error on the EditText to highlight the issue
                editText.setError("Invalid number");
                return 0.0; // Handle invalid input by returning 0.0
            }
        }
    }
}
