package com.example.b07group57;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.example.b07group57.models.DailyDataLoader;
import com.example.b07group57.models.EcoTrackerEmissionsCalculator;
import com.example.b07group57.models.EcoTrackerFragmentModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EcoTrackerFragment extends Fragment {
    private String selectedDate, selectedFuelType;
    private EditText driveInput, cyclingWalkingInput, busInput, trainInput, subwayInput, shortFlightInput,
            longFlightInput, beefInput, porkInput, chickenInput, fishInput, plantBasedInput,
            clothingInput, electricityBillsInput, gasBillsInput, waterBillsInput;
    private Button btnEdit, addElectronicDeviceButton, addOtherButton;
    protected Spinner fuelTypeSpinner;
    private boolean isEditable = false;
    private List<TextView> deleteTextList = new ArrayList<>();
    private List<LinearLayout> electronicsInputs = new ArrayList<>();
    private List<LinearLayout> otherInputs = new ArrayList<>();
    private List<EditText> inputTypeTextList = new ArrayList<>();
    private List<EditText> inputTextList = new ArrayList<>();
    protected ArrayAdapter<CharSequence> adapter;
    private boolean isValid;

    public EcoTrackerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eco_tracker_fragment, container, false);

        //Grabs the current date the user has selected
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate", "");
        }

        ((MainActivity) getActivity()).showNavigationBar(true);
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

        isValid = true;
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
        addElectronicDeviceButton.setOnClickListener(v -> {
            addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs, "electronics");
            enableSaveButtonIfValid();
        });
        addOtherButton.setOnClickListener(v -> {
            addNewInput("type (e.g. furniture, appliances)", otherPairs, "other");
            enableSaveButtonIfValid();
        });

        btnEdit.setOnClickListener(v -> {
            if (isEditable) {
                setSpinnerEditable(false);
                btnEdit.setText("Edit");
                setAddDeleteButtonsEnabled(false);
                enableEditText(false);
                calculateEmissions();
                savetoDB();
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
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.fuel_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(adapter);
        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedFuelType = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showSavedData(selectedDate, new TrackerDataLoadedCallBack() {
            @Override
            public void onDataLoaded() {
                enableSaveButtonIfValid();
                enableEditText(false);
                setSpinnerEditable(false);
                btnEdit.setText("Edit");
                setAddDeleteButtonsEnabled(false);
                calculateEmissions();
            }
        });
    }

    interface TrackerDataLoadedCallBack {
        void onDataLoaded();
    }
    // Function to get data from firebase and set default UI
    private void showSavedData(String date, TrackerDataLoadedCallBack callback) {
        final int totalTasks = 3;
        final AtomicInteger completedTasks = new AtomicInteger(0);

        DailyDataLoader dailyDataLoader = new DailyDataLoader();
        dailyDataLoader.loadInputData(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> inputData) {
                // If there exists a data
                if (inputData != null) {
                    for (Map.Entry<String, Object> entry : inputData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        switch (key) {
                            case "Gasoline":
                                fuelTypeSpinner.setSelection(adapter.getPosition("Gasoline"));
                                selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();
                                driveInput.setText(String.valueOf(value));
                                break;
                            case "Diesel":
                                fuelTypeSpinner.setSelection(adapter.getPosition("Diesel"));
                                selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();
                                driveInput.setText(String.valueOf(value));
                                break;
                            case "Hybrid":
                                fuelTypeSpinner.setSelection(adapter.getPosition("Hybrid"));
                                selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();
                                driveInput.setText(String.valueOf(value));
                                break;
                            case "Electric":
                                fuelTypeSpinner.setSelection(adapter.getPosition("Electric"));
                                selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();
                                driveInput.setText(String.valueOf(value));
                                break;
                            case "CyclingWalking":
                                cyclingWalkingInput.setText(String.valueOf(value));
                                break;
                            case "Bus":
                                busInput.setText(String.valueOf(value));
                                break;
                            case "Train":
                                trainInput.setText(String.valueOf(value));
                                break;
                            case "Subway":
                                subwayInput.setText(String.valueOf(value));
                                break;
                            case "ShortFlight":
                                shortFlightInput.setText(String.valueOf(value));
                                break;
                            case "LongFlight":
                                longFlightInput.setText(String.valueOf(value));
                                break;
                            case "Beef":
                                beefInput.setText(String.valueOf(value));
                                break;
                            case "Pork":
                                porkInput.setText(String.valueOf(value));
                                break;
                            case "Chicken":
                                chickenInput.setText(String.valueOf(value));
                                break;
                            case "Fish":
                                fishInput.setText(String.valueOf(value));
                                break;
                            case "PlantBased":
                                plantBasedInput.setText(String.valueOf(value));
                                break;
                            case "Clothing":
                                clothingInput.setText(String.valueOf(value));
                                break;
                            case "Electricity":
                                electricityBillsInput.setText(String.valueOf(value));
                                break;
                            case "Gas":
                                gasBillsInput.setText(String.valueOf(value));
                                break;
                            case "Water":
                                waterBillsInput.setText(String.valueOf(value));
                        }
                    }
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date);

        dailyDataLoader.loadElectronicsOrOtherData(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> electronicsOrOtherData) {
                if (electronicsOrOtherData != null) {
                    for (Map.Entry<String, Object> entry : electronicsOrOtherData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        LinearLayout devicePairs = getView().findViewById(R.id.devicePairs);
                        LinearLayout newPair = addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs, "electronics");
                        EditText newInputType = (EditText) newPair.getChildAt(1);
                        newInputType.setText(key);
                        EditText newInputValue = (EditText) newPair.getChildAt(2);
                        newInputValue.setText(String.valueOf(value));
                    }
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date, "electronics");

        dailyDataLoader.loadElectronicsOrOtherData(new DailyDataLoader.DataLoadCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> electronicsOrOtherData) {
                if (electronicsOrOtherData != null) {
                    for (Map.Entry<String, Object> entry : electronicsOrOtherData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        LinearLayout otherPairs = getView().findViewById(R.id.otherPairs);
                        LinearLayout newPair = addNewInput("type (e.g. furniture, appliances)", otherPairs, "other");
                        EditText newInputType = (EditText) newPair.getChildAt(1);
                        newInputType.setText(key);
                        EditText newInputValue = (EditText) newPair.getChildAt(2);
                        newInputValue.setText(String.valueOf(value));
                    }
                }
                checkIfAllTasksCompleted(callback, completedTasks, totalTasks);
            }
        }, date, "other");
    }

    private void checkIfAllTasksCompleted(TrackerDataLoadedCallBack callback, AtomicInteger completedTasks, int totalTasks) {
        if (completedTasks.incrementAndGet() == totalTasks) {
            if (callback != null) {
                callback.onDataLoaded();
            }
        }
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

    private LinearLayout addNewInput(String type, LinearLayout parent, String category) {
        LinearLayout newPair = new LinearLayout(getContext());
        newPair.setOrientation(LinearLayout.VERTICAL);

        EditText newTypeInput = new EditText(getContext());
        newTypeInput.setHint(type);
        EditText newInput = new EditText(getContext());
        newInput.setHint("(quantity)");
        newInput.setInputType(TYPE_CLASS_NUMBER);

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

        newTypeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                enableSaveButtonIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableSaveButtonIfValid();
            }
        });

        newInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                enableSaveButtonIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableSaveButtonIfValid();
            }
        });

        // Store the input data in the appropriate category
        if ("electronics".equals(category)) {
            electronicsInputs.add(newPair);  // Store in electronics inputs list
        } else if ("other".equals(category)) {
            otherInputs.add(newPair);  // Store in other inputs list
        }

        return newPair;
    }

    private void enableSaveButtonIfValid() {
        boolean tempValid = true;
        for (EditText typeInput : inputTypeTextList) {
            if (!validateInput(typeInput)) {
                tempValid = false;
            }
        }

        isValid = tempValid;
        btnEdit.setEnabled(isValid);
    }

    private boolean validateInput(EditText input) {
        String text = input.getText().toString();

        if (text.trim().isEmpty()) {
            input.setError("This field cannot be empty");
            return false;
        }
        if (containsInvalidCharacters(text)) {
            input.setError("Input contains invalid characters");
            return false;
        }
        return true;
    }

    private boolean containsInvalidCharacters(String text) {
        return text.contains("/") || text.contains(".") || text.contains("#")
                || text.contains("$") || text.contains("[") || text.contains("]");
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
            enableSaveButtonIfValid();
        });
        deleteTextList.add(deleteText);

        return deleteText;
    }

    public void deletePair(ViewGroup parentLayout) {
        if (parentLayout != null && parentLayout.getParent() instanceof ViewGroup) {
            EditText typeInput = (EditText) parentLayout.getChildAt(1);
            EditText quantityInput = (EditText) parentLayout.getChildAt(2);
            ViewGroup grandParentLayout = (ViewGroup) parentLayout.getParent();
            inputTypeTextList.remove(typeInput);
            Log.d("typeInput", "Value : " + typeInput.getText()); // for debug
            inputTextList.remove(quantityInput);
            Log.d("quantityInput", "Value : " + quantityInput.getText()); // for debug
            electronicsInputs.remove(parentLayout);
            otherInputs.remove(parentLayout);
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
        keyListener(driveInput, isEnabled);
        driveInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(cyclingWalkingInput, isEnabled);
        cyclingWalkingInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(busInput, isEnabled);
        busInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(trainInput, isEnabled);
        trainInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(subwayInput, isEnabled);
        subwayInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(shortFlightInput, isEnabled);
        shortFlightInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        keyListener(longFlightInput, isEnabled);
        longFlightInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        keyListener(beefInput, isEnabled);
        beefInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(porkInput, isEnabled);
        porkInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(chickenInput, isEnabled);
        chickenInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(fishInput, isEnabled);
        fishInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(plantBasedInput, isEnabled);
        plantBasedInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(clothingInput, isEnabled);
        clothingInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        keyListener(electricityBillsInput, isEnabled);
        electricityBillsInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(gasBillsInput, isEnabled);
        gasBillsInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        keyListener(waterBillsInput, isEnabled);
        waterBillsInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        for (EditText inputTypeText : inputTypeTextList) {
            keyListener(inputTypeText, isEnabled);
            inputTypeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        for (EditText inputText : inputTextList) {
            keyListener(inputText, isEnabled);
        }
    }

    private void keyListener (EditText text, boolean isEnabled) {
        text.setFocusable(isEnabled);
        text.setFocusableInTouchMode(isEnabled);
        if (!isEnabled) {
            text.setKeyListener(null);
        } else {
            text.setKeyListener(TextKeyListener.getInstance());
        }
    }

    private void savetoDB() {
        // Collect data from all inputs
        HashMap<String, Object> inputData = new HashMap<>();
        inputData.put(selectedFuelType, getDoubleFromEditText(driveInput));
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

            String type = typeInput.getText().toString();
            double quantity = getDoubleFromEditText(quantityInput);

            // Check if the key exists, and if so, add the new value to the existing value
            if (electronicsData.containsKey(type)) {
                double existingQuantity = (double) electronicsData.get(type);
                electronicsData.put(type, existingQuantity + quantity);
            } else {
                electronicsData.put(type, quantity);
            }
        }

        // Collect Other Inputs
        HashMap<String, Object> otherData = new HashMap<>();
        for (int i = 0; i < otherInputs.size(); i++) {
            EditText typeInput = (EditText) otherInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) otherInputs.get(i).getChildAt(2);  // Get quantity input

            String type = typeInput.getText().toString();
            double quantity = getDoubleFromEditText(quantityInput);

            // Check if the key exists, and if so, add the new value to the existing value
            if (otherData.containsKey(type)) {
                double existingQuantity = (double) otherData.get(type);
                otherData.put(type, existingQuantity + quantity);
            } else {
                otherData.put(type, quantity);
            }
        }

//        // Combine all inputs
//        inputData.putAll(electronicsData);
//        inputData.putAll(otherData);

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

            String type = typeInput.getText().toString();
            if (electronicsDataCO2.containsKey(type)) {
                // Key exists, add emissions to the existing value
                double existingEmissions = (double) electronicsDataCO2.get(type);
                electronicsDataCO2.put(type, existingEmissions + emissions);
            } else {
                // New key, add it to the map
                electronicsDataCO2.put(type, emissions);
            }
        }

        // Collect Other Inputs
        HashMap<String, Object> otherDataCO2 = new HashMap<>();
        for (int i = 0; i < otherInputs.size(); i++) {
            EditText typeInput = (EditText) otherInputs.get(i).getChildAt(1);  // Get type input
            EditText quantityInput = (EditText) otherInputs.get(i).getChildAt(2);  // Get quantity input
            double emissions = EcoTrackerEmissionsCalculator.calculateEmissions("Other Purchases", getDoubleFromEditText(quantityInput), "");

            String type = typeInput.getText().toString();
            if (otherDataCO2.containsKey(type)) {
                // Key exists, add emissions to the existing value
                double existingEmissions = (double) otherDataCO2.get(type);
                otherDataCO2.put(type, existingEmissions + emissions);
            } else {
                // New key, add it to the map
                otherDataCO2.put(type, emissions);
            }
        }

        EcoTrackerFragmentModel m = new EcoTrackerFragmentModel();
        m.saveDailyInputDB(inputData, electronicsData, otherData, co2eData, electronicsDataCO2, otherDataCO2, selectedDate);
    }

    private void logMap(String tag, HashMap<String, Object> map) {
        for (String key : map.keySet()) {
            Log.d(tag, "Key: " + key + ", Value: " + map.get(key));
        }
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
    public double calculateEmissions() {
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

        for (int i = 0; i < electronicsInputs.size(); i++) {// Get type input
            EditText quantityInput = (EditText) electronicsInputs.get(i).getChildAt(2);  // Get quantity input
            totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Buy Electronics", getDoubleFromEditText(quantityInput), "");
        }

        for (int i = 0; i < otherInputs.size(); i++) {
            EditText quantityInput = (EditText) otherInputs.get(i).getChildAt(2);  // Get quantity input
            totalEmissions += EcoTrackerEmissionsCalculator.calculateEmissions("Other Purchases", getDoubleFromEditText(quantityInput), "");
        }


        // Update the result TextView
        TextView emissionsResultText = getView().findViewById(R.id.emissionsResultText);
        emissionsResultText.setText(String.format("Emissions: %.2f kg CO2e", totalEmissions));
        return totalEmissions;
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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
