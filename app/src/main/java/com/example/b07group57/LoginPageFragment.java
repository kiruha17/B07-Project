package com.example.b07group57;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPageFragment extends Fragment {
    // FirebaseAuth
    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button loginButton, forgotPasswordButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page_fragment, container, false);

        // Initialize views
        emailField = view.findViewById(R.id.email);
        passwordField = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        forgotPasswordButton = view.findViewById(R.id.forgot_password_button);

        // Initially disable the login button
        loginButton.setEnabled(false);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Check login state from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        long loginTimestamp = sharedPreferences.getLong("loginTimestamp", 0);

        long currentTime = System.currentTimeMillis();
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;  // Convert 30 days into milliseconds

        if (isLoggedIn) {
            if (currentTime - loginTimestamp < thirtyDaysInMillis) {
                // If login session is still valid, move to top page
                navigateTopPage();
            } else {
                // If the session has expired, make the user log in again
                Toast.makeText(requireActivity(), "Your session has expired. Please log in again.", Toast.LENGTH_SHORT).show();
                resetState();
            }
        } else {
            Toast.makeText(requireActivity(), "Please log in.", Toast.LENGTH_SHORT).show();
        }

        // Add a TextWatcher to dynamically enable the button
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmailFilled = !TextUtils.isEmpty(emailField.getText().toString());
                boolean isPasswordFilled = !TextUtils.isEmpty(passwordField.getText().toString());

                // Enable the button only if both fields are filled
                loginButton.setEnabled(isEmailFilled && isPasswordFilled);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Attach the TextWatcher to both fields
        emailField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Firebase login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    // Success
                    saveLoginState();
                    navigateTopPage();
                } else {
                    // Handle specific Firebase exceptions
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
                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        forgotPasswordButton.setOnClickListener(v -> navigateToResetPasswordPage());

        return view;
    }

    private void navigateToResetPasswordPage() {
        // Navigate to ResetPasswordEmailInputFragment
        Fragment resetPasswordFragment = new ResetPasswordFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resetPasswordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateTopPage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Check in Firebase if the user has already completed the survey
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.child("surveyData").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // If survey data exists, navigate to MainMenuFragment
                        navigateToMainMenu();
                    } else {
                        // If survey data does not exist, navigate to AnnualCarbonFootprintSurveyFragment
                        navigateToSurvey();
                    }
                } else {
                    Toast.makeText(requireContext(), "Error retrieving user data: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToMainMenu() {
        Fragment mainMenuFragment = new MainMenuFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainMenuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToSurvey() {
        Fragment surveyFragment = new AnnualCarbonFootprintSurveyFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, surveyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("loginTimestamp", System.currentTimeMillis()); // Save timestamp of login date
        editor.apply();
    }

    private void resetState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // Reset login state
        editor.putLong("loginTimestamp", 0);   // Reset timestamp
        editor.apply();
    }
}