package com.example.b07group57.utils;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;


public class EcoGaugeCalculator {

    private HashMap<String, Integer> combinedData; // Class variable to store the data

    public EcoGaugeCalculator() {
        this.combinedData = new HashMap<>(); // Initialize the map
    }

    public void getCombinedData(String selectedDate) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference dailyDataRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(userId)
                    .child("ecoTrackerDaily")
                    .child(selectedDate);

            dailyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        combinedData.clear(); // Clear any existing data

                        // Retrieve the co2e node
                        HashMap<String, Object> co2e = (HashMap<String, Object>) dataSnapshot.child("co2e").getValue();
                        if (co2e != null) {
                            for (String key : co2e.keySet()) {
                                // Check if the value is a nested map (electronics/other)
                                Object value = co2e.get(key);
                                if (value instanceof Map) {
                                    // Flatten nested maps like electronics and other
                                    HashMap<String, Integer> nestedMap = (HashMap<String, Integer>) value;
                                    for (String nestedKey : nestedMap.keySet()) {
                                        combinedData.put(nestedKey, nestedMap.get(nestedKey));
                                    }
                                } else if (value instanceof Number) {
                                    // Add other integer values directly
                                    combinedData.put(key, ((Number) value).intValue());
                                }
                            }
                        }

                        // Data is now stored in the combinedData variable
                        sortList(); // Call the method to process the data
                    } else {
                        System.err.println("No data exists for the selected date.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.err.println("Error: " + databaseError.getMessage());
                }
            });
        } else {
            System.err.println("User not authenticated.");
        }
    }

    private float getTotal() {
        return combinedData.values().stream().mapToInt(Integer::intValue).sum();
    }
    // Method to process the data after retrieval
    //private HashMap<String, Integer> sortList() {
    private void sortList() {
        // Example: Sort by value (highest to lowest)
        combinedData.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    public HashMap<String, Integer> getCombinedDataMap() {
        return combinedData;
    }
}
