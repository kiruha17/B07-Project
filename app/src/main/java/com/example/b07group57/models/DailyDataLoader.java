package com.example.b07group57.models;

import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class DailyDataLoader {

    private String userId;
    private DatabaseReference db;

    public DailyDataLoader() {
        this.db = FirebaseDatabase.getInstance().getReference();
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public interface DataLoadCallback {
        void onDataLoaded(HashMap<String, Object> inputData);
    }

    public void loadInputData(DataLoadCallback callBack, String date) {
        HashMap<String, Object> inputData = new HashMap<>();
        db.child("users").child(userId).child("ecoTrackerDaily").child(date)
                .child("inputAmount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (!(snapshot.getValue() instanceof HashMap)) {
                                    inputData.put(snapshot.getKey(), snapshot.getValue());
                                }
                            }
                        }
                        callBack.onDataLoaded(inputData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callBack.onDataLoaded(null);
                    }
                });
    }

    public void loadElectronicsOrOtherData(DataLoadCallback callBack, String date, String key /* "electronics or other" */ ) {
        HashMap<String, Object> electronicsOrOtherData = new HashMap<>();
        db.child("users").child(userId).child("ecoTrackerDaily").child(date)
                .child("inputAmount")
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (!(snapshot.getValue() instanceof HashMap)) {
                                    electronicsOrOtherData.put(snapshot.getKey(), snapshot.getValue());
                                }
                            }
                        }
                        callBack.onDataLoaded(electronicsOrOtherData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callBack.onDataLoaded(null);
                    }
                });
    }

    public void loadInputCO2Data(DataLoadCallback callBack, String date) {
        HashMap<String, Object> inputCO2Data = new HashMap<>();
        db.child("users").child(userId).child("ecoTrackerDaily").child(date)
                .child("co2e")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (!(snapshot.getValue() instanceof HashMap)) {
                                    inputCO2Data.put(snapshot.getKey(), snapshot.getValue());
                                }
                            }
                        }
                        callBack.onDataLoaded(inputCO2Data.isEmpty() ? null : inputCO2Data);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callBack.onDataLoaded(null);
                    }
                });
    }

    public void loadElectronicsOrOtherCO2Data(DataLoadCallback callBack, String date, String key /* "electronics" or "other" */) {
        HashMap<String, Object> electronicsOrOtherCO2Data = new HashMap<>();
        db.child("users").child(userId).child("ecoTrackerDaily").child(date)
                .child("co2e")
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (!(snapshot.getValue() instanceof HashMap)) {
                                    electronicsOrOtherCO2Data.put(snapshot.getKey(), snapshot.getValue());
                                }
                            }
                        }
                        callBack.onDataLoaded(electronicsOrOtherCO2Data.isEmpty() ? null : electronicsOrOtherCO2Data);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callBack.onDataLoaded(null);
                    }
                });
    }
}

