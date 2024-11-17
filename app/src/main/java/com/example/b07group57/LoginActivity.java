package com.example.b07group57;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuthenticationActivity extends AppCompatActivity {
    // FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_fragment);  // Layout
        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Get UI components
        Button loginButton = findViewById(R.id.login_button);
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);
        // On click action
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(UserAuthenticationActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Firebase login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Success
                    saveLoginState();
                    startActivity(new Intent(this, HomeActivity.class)); // HomeActivity.class
                    finish();
                } else {
                    // Fail
                    Toast.makeText(UserAuthenticationActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Get SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        // If already logged in, move to homepage
        if (isLoggedIn) {
            startActivity(new Intent(UserAuthenticationActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
}

