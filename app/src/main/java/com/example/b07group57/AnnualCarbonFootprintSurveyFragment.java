package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnnualCarbonFootprintSurveyFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.annual_carbon_footprint_survey_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Spinner spinnerCountry = view.findViewById(R.id.spinner_country);
        RadioGroup radioGroupCarOwner = view.findViewById(R.id.car_owner_or_not);
        View carQuestionsSection = view.findViewById(R.id.car_questions_section);
        Spinner spinnerCarType = view.findViewById(R.id.spinner_car_type);
        Spinner spinnerPublicTransportFreq = view.findViewById(R.id.spinner_public_transport_freq);
        Spinner spinnerDistanceDriven = view.findViewById(R.id.spinner_distance_driven);
        Spinner spinnerTimeSpentOnPublicTransport = view.findViewById(R.id.spinner_time_spent_on_public_transport);
        Spinner spinnerShortHaulFlights = view.findViewById(R.id.spinner_short_haul_flights);
        Spinner spinnerLongHaulFlights = view.findViewById(R.id.spinner_long_haul_flights);
        RadioGroup radioGroupDiet = view.findViewById(R.id.diet);
        View meatQuestionsSection = view.findViewById(R.id.meat_questions_section);
        Spinner spinnerBeef = view.findViewById(R.id.spinner_beef);
        Spinner spinnerPork = view.findViewById(R.id.spinner_pork);
        Spinner spinnerChicken = view.findViewById(R.id.spinner_chicken);
        Spinner spinnerFish = view.findViewById(R.id.spinner_fish);
        Spinner spinnerFoodWaste = view.findViewById(R.id.spinner_food_waste);
        Spinner spinnerHomeType = view.findViewById(R.id.spinner_home_type);
        Spinner spinnerHomePeople = view.findViewById(R.id.spinner_home_people);
        Spinner spinnerHomeSize = view.findViewById(R.id.spinner_home_size);
        Spinner spinnerHomeHeater = view.findViewById(R.id.spinner_home_heater);
        Spinner spinnerElectricityBill = view.findViewById(R.id.spinner_electricity_bill);
        Spinner spinnerWaterHeater = view.findViewById(R.id.spinner_water_heater);
        Spinner spinnerRenewableEnergy = view.findViewById(R.id.spinner_renewable_energy);
        Spinner spinnerBuyClothes = view.findViewById(R.id.spinner_buy_clothes);
        Spinner spinnerBuySecondHand = view.findViewById(R.id.spinner_buy_second_hand);
        Spinner spinnerBuyElectronics = view.findViewById(R.id.spinner_buy_electronics);
        Spinner spinnerRecycle = view.findViewById(R.id.spinner_recycle);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.country_list,
                R.layout.spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        radioGroupCarOwner.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_yes) {
                // Show car-related questions
                carQuestionsSection.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_no) {
                // Hide car-related questions
                carQuestionsSection.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<CharSequence> carTypeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.car_type,
                R.layout.spinner_item);
        carTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(carTypeAdapter);

        ArrayAdapter<CharSequence> publicTransportFreqAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.public_transport_freq,
                R.layout.spinner_item);
        publicTransportFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublicTransportFreq.setAdapter(publicTransportFreqAdapter);

        ArrayAdapter<CharSequence> distanceDrivenAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.distance_driven,
                R.layout.spinner_item);
        distanceDrivenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistanceDriven.setAdapter(distanceDrivenAdapter);

        ArrayAdapter<CharSequence> timeSpentOnPublicTransportAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.time_spent_on_public_transport,
                R.layout.spinner_item);
        timeSpentOnPublicTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSpentOnPublicTransport.setAdapter(timeSpentOnPublicTransportAdapter);

        ArrayAdapter<CharSequence> shortHaulFlightAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.short_haul_flights,
                R.layout.spinner_item);
        shortHaulFlightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShortHaulFlights.setAdapter(shortHaulFlightAdapter);

        ArrayAdapter<CharSequence> longHaulFlightAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.long_haul_flights,
                R.layout.spinner_item);
        longHaulFlightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLongHaulFlights.setAdapter(longHaulFlightAdapter);

        ArrayAdapter<CharSequence> beefAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.meat_freq,
                R.layout.spinner_item);
        beefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBeef.setAdapter(beefAdapter);

        ArrayAdapter<CharSequence> porkAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.meat_freq,
                R.layout.spinner_item);
        porkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPork.setAdapter(porkAdapter);

        ArrayAdapter<CharSequence> chickenAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.meat_freq,
                R.layout.spinner_item);
        chickenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChicken.setAdapter(chickenAdapter);

        ArrayAdapter<CharSequence> fishAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.meat_freq,
                R.layout.spinner_item);
        fishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFish.setAdapter(fishAdapter);

        radioGroupDiet.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_meat_based) {
                // Show meat-related questions
                meatQuestionsSection.setVisibility(View.VISIBLE);
            } else {
                // Hide meat-related questions
                meatQuestionsSection.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<CharSequence> foodWasteAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.food_waste,
                R.layout.spinner_item);
        foodWasteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodWaste.setAdapter(foodWasteAdapter);

        ArrayAdapter<CharSequence> homeTypeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.home_type,
                R.layout.spinner_item);
        homeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHomeType.setAdapter(homeTypeAdapter);

        ArrayAdapter<CharSequence> homePeopleAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.home_people,
                R.layout.spinner_item);
        homePeopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHomePeople.setAdapter(homePeopleAdapter);

        ArrayAdapter<CharSequence> homeSizeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.home_size,
                R.layout.spinner_item);
        homeSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHomeSize.setAdapter(homeSizeAdapter);

        ArrayAdapter<CharSequence> homeHeaterAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.home_heater,
                R.layout.spinner_item);
        homeHeaterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHomeHeater.setAdapter(homeHeaterAdapter);

        ArrayAdapter<CharSequence> electricityBillAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.electricity_bill,
                R.layout.spinner_item);
        electricityBillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerElectricityBill.setAdapter(electricityBillAdapter);

        ArrayAdapter<CharSequence> waterHeaterAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.water_heater,
                R.layout.spinner_item);
        waterHeaterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWaterHeater.setAdapter(waterHeaterAdapter);

        ArrayAdapter<CharSequence> renewableEnergyAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.renewable_energy,
                R.layout.spinner_item);
        renewableEnergyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRenewableEnergy.setAdapter(renewableEnergyAdapter);

        ArrayAdapter<CharSequence> buyClothesAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.buy_clothes,
                R.layout.spinner_item);
        buyClothesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuyClothes.setAdapter(buyClothesAdapter);

        ArrayAdapter<CharSequence> buySecondHandAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.buy_second_hand,
                R.layout.spinner_item);
        buySecondHandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuySecondHand.setAdapter(buySecondHandAdapter);

        ArrayAdapter<CharSequence> buyElectronics = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.buy_electronics,
                R.layout.spinner_item);
        buyElectronics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuyElectronics.setAdapter(buyElectronics);

        ArrayAdapter<CharSequence> recycleAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.recycle,
                R.layout.spinner_item);
        recycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecycle.setAdapter(recycleAdapter);

        confirmButton.setOnClickListener(v -> {
            try {
                // Create a map to store user responses
                Map<String, Object> surveyData = new HashMap<>();
                surveyData.put("country", spinnerCountry.getSelectedItem().toString());

                int carOwnerId = radioGroupCarOwner.getCheckedRadioButtonId();
                RadioButton carOwnerButton = view.findViewById(carOwnerId);
                surveyData.put("carOwner", carOwnerButton.getText().toString());

                surveyData.put("carType", spinnerCarType.getSelectedItem().toString());
                surveyData.put("publicTransportFreq", spinnerPublicTransportFreq.getSelectedItem().toString());
                surveyData.put("distanceDriven", spinnerDistanceDriven.getSelectedItem().toString());
                surveyData.put("timeOnPublicTransport", spinnerTimeSpentOnPublicTransport.getSelectedItem().toString());
                surveyData.put("shortHaulFlights", spinnerShortHaulFlights.getSelectedItem().toString());
                surveyData.put("longHaulFlights", spinnerLongHaulFlights.getSelectedItem().toString());

                int dietId = radioGroupDiet.getCheckedRadioButtonId();
                RadioButton dietButton = view.findViewById(dietId);
                surveyData.put("diet", dietButton.getText().toString());

                surveyData.put("beefConsumption", spinnerBeef.getSelectedItem().toString());
                surveyData.put("porkConsumption", spinnerPork.getSelectedItem().toString());
                surveyData.put("chickenConsumption", spinnerChicken.getSelectedItem().toString());
                surveyData.put("fishConsumption", spinnerFish.getSelectedItem().toString());
                surveyData.put("foodWaste", spinnerFoodWaste.getSelectedItem().toString());

                surveyData.put("homeType", spinnerHomeType.getSelectedItem().toString());
                surveyData.put("homePeople", spinnerHomePeople.getSelectedItem().toString());
                surveyData.put("homeSize", spinnerHomeSize.getSelectedItem().toString());
                surveyData.put("homeHeater", spinnerHomeHeater.getSelectedItem().toString());
                surveyData.put("electricityBill", spinnerElectricityBill.getSelectedItem().toString());
                surveyData.put("waterHeater", spinnerWaterHeater.getSelectedItem().toString());
                surveyData.put("renewableEnergy", spinnerRenewableEnergy.getSelectedItem().toString());

                surveyData.put("buyClothes", spinnerBuyClothes.getSelectedItem().toString());
                surveyData.put("buySecondHand", spinnerBuySecondHand.getSelectedItem().toString());
                surveyData.put("buyElectronics", spinnerBuyElectronics.getSelectedItem().toString());
                surveyData.put("recycle", spinnerRecycle.getSelectedItem().toString());

                // Convert survey data to JSON (optional for debugging)
                JSONObject surveyJson = new JSONObject(surveyData);
                System.out.println(surveyJson);

                // Store the data in Firebase under the user's UID
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    databaseReference.child("users").child(userId).child("surveyData").setValue(surveyData)
                            .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Survey submitted successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to submit survey: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(requireContext(), "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(requireContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}