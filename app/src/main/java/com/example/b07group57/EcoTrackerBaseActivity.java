package com.example.b07group57;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class EcoTrackerBaseActivity extends AppCompatActivity {
    protected abstract void initializeViews(); //Initialize UI elements
    protected abstract boolean validateInputs();
    protected abstract void saveData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        initializeViews();

        Button saveButton = findViewById(getSaveButtonId());
        if (saveButton != null){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if (validateInputs()){
                        saveData();
                        finish();
                    }
                }
            });
        }
    }

    protected abstract int getLayoutResourceId();
    protected abstract int getSaveButtonId();
}
