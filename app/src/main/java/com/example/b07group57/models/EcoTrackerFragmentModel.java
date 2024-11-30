package com.example.b07group57.models;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EcoTrackerFragmentModel {
    private DatabaseReference db;

    private FirebaseAuth mAuth;

    public EcoTrackerFragmentModel(){
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void saveDailyInputDB(HashMap<String, Object> inputData, HashMap<String, Object> electronics, HashMap<String, Object> other,
                                 HashMap<String, Object> co2EData, HashMap<String, Object> electronicCo2, HashMap<String, Object> otherCo2, String selectedDate){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.d("EcoTrackerModel", "Selected date: " + selectedDate);

        // saveDailyInputDB
        logMap("EcoTrackerModel-inputData", inputData);
        logMap("EcoTrackerModel-electronics", electronics);
        logMap("EcoTrackerModel-other", other);
        logMap("EcoTrackerModel-co2EData", co2EData);
        logMap("EcoTrackerModel-electronicCo2", electronicCo2);
        logMap("EcoTrackerModel-otherCo2", otherCo2);


        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").setValue(inputData);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").child("electronics").setValue(electronics);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").child("other").setValue(other);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").setValue(co2EData);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").child("electronics").setValue(electronicCo2);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").child("other").setValue(otherCo2);
        }
    }

    private void logMap(String tag, HashMap<String, Object> map) {
        for (String key : map.keySet()) {
            Log.d(tag, "Key: " + key + ", Value: " + map.get(key));
        }
    }
}
