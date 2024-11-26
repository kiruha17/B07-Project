package com.example.b07group57.models;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.b07group57.models.SurveyData;

public class ACFCalculator {
    private final DatabaseReference usersRef;

    public ACFCalculator() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    // Fetch survey data and calculate carbon footprint
    public void calculateCarbonFootprint(String userId) {
        DatabaseReference surveyDataRef = usersRef.child(userId).child("surveyData");
        surveyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SurveyData surveyData = snapshot.getValue(SurveyData.class);
                if (surveyData != null) {
                    double carbonFootprint = calculateFootprint(surveyData);
                    System.out.println("User's Annual Carbon Footprint: " + carbonFootprint + " kg CO2");
                } else {
                    System.out.println("No survey data found for user: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to read data: " + error.getMessage());
            }
        });
    }

    // Calculate carbon footprint based on survey data
    private double calculateFootprint(SurveyData data) {
        double footprint = 0;

        //Placeholder Reference, delete afterwards
        //---------------------------------------
        //---------------------------------------
        if (data.beefConsumption != null && data.beefConsumption.equalsIgnoreCase("Daily")) {
            footprint += 1500; // Example: kg CO2/year
        }
        //---------------------------------------
        //---------------------------------------
        //---------------------------------------

        return footprint;
    }
}