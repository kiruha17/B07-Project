package com.example.b07group57;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    // FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_fragment);  // Layout
        // initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // get UI components
        Button loginButton = findViewById(R.id.login_button);
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);

        // check login state from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        long loginTimestamp = sharedPreferences.getLong("loginTimestamp", 0);

        long currentTime = System.currentTimeMillis();
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;  // 30日をミリ秒に変換

        if (isLoggedIn) {
            if (currentTime - loginTimestamp < thirtyDaysInMillis) {
                // if login session is still valid, move to homepage
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                // if the session has expired, make user login again
                Toast.makeText(this, "Your session has expired. Please log in again.", Toast.LENGTH_SHORT).show();
                resetState();
            }
        } else {
            Toast.makeText(this, "Please log in.", Toast.LENGTH_SHORT).show();
        }

        // set on click action
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Firebase login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // success
                    saveLoginState();
                    startActivity(new Intent(this, HomeActivity.class)); // HomeActivity.class
                    finish();
                }
                else {
                    // handle specific Firebase exceptions
                    String errorMessage;
                    if (task.getException() != null) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Invalid credentials. Please check your email and password.";
                        } catch (FirebaseAuthInvalidUserException e) {
                            errorMessage = "No account found with this email.";
                        } catch (Exception e) {
                            errorMessage = "Authentication failed: " + e.getMessage();
                        }
                    } else {
                        errorMessage = "Unknown error occurred.";
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("loginTimestamp", System.currentTimeMillis()); // Save time stamp of login date
        editor.apply();
    }

    private void resetState() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // reset login state
        editor.putLong("loginTimestamp", 0);   // reset time stamp
        editor.apply();
    }
}

