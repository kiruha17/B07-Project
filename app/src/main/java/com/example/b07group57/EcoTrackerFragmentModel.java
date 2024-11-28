package com.example.b07group57;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EcoTrackerFragmentModel {
    private DatabaseReference db;

    private FirebaseAuth mAuth;

    public EcoTrackerFragmentModel(){
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        
    }

}
