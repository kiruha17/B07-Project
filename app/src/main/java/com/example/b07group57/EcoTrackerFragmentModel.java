package com.example.b07group57;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class EcoTrackerFragmentModel {
    private DatabaseReference db;

    private FirebaseAuth mAuth;

    public EcoTrackerFragmentModel(){
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    void saveDailyInputDB(HashMap<String, Object> inputData, HashMap<String, Object> electronics, HashMap<String, Object> other,
                          HashMap<String, Object> co2EData, HashMap<String, Object> electronicCo2, HashMap<String, Object> otherCo2, String selectedDate){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            String userId = currentUser.getUid();
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").setValue(inputData);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").child("electronics").setValue(electronics);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("inputAmount").child("other").setValue(other);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").setValue(co2EData);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").child("electronics").setValue(electronicCo2);
            db.child("users").child(userId).child("ecoTrackerDaily").child(selectedDate).child("co2e").child("other").setValue(otherCo2);
        }
    }

    public void getDailyInputData(String selectedDate) {
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
                        // Retrieve and process data
                        HashMap<String, Object> co2e = (HashMap<String, Object>) dataSnapshot.child("co2e").getValue();
                        HashMap<String, Integer> electronics = (HashMap<String, Integer>) dataSnapshot.child("co2e").child("electronics").getValue();
                        HashMap<String, Integer> other = (HashMap<String, Integer>) dataSnapshot.child("co2e").child("other").getValue();

                        // Process the data
                        processCo2eData(co2e, electronics, other);
                    } else {
                        Log.e("FirebaseData", "No data exists for the selected date.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("FirebaseData", "User not authenticated.");
        }
    }


}
