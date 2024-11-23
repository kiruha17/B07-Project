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

public class LoginPageFragment extends Fragment {
    // FirebaseAuth
    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button loginButton, forgotPasswordButton;
    private Activity activity = getActivity();

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

        // initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // check login state from SharedPreferences
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        long loginTimestamp = sharedPreferences.getLong("loginTimestamp", 0);

        long currentTime = System.currentTimeMillis();
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;  // convert 30 days into millisecond

        if (isLoggedIn) {
            if (currentTime - loginTimestamp < thirtyDaysInMillis) {
                // if login session is still valid, move to top page
                navigateTopPage();
            }
            else {
                // if the session has expired, make user login again
                Toast.makeText(getActivity(), "Your session has expired. Please log in again.", Toast.LENGTH_SHORT).show();
                resetState();
            }
        } else {
            Toast.makeText(activity, "Please log in.", Toast.LENGTH_SHORT).show();
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
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    // success
                    saveLoginState();
                    navigateTopPage();
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
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        forgotPasswordButton.setOnClickListener(v -> {
            navigateToResetPasswordPage();
        });

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
        // Navigate to ResetPasswordEmailInputFragment
        Fragment TopPageFragment = new TopPageFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, TopPageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("loginTimestamp", System.currentTimeMillis()); // Save time stamp of login date
        editor.apply();
    }

    private void resetState() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // reset login state
        editor.putLong("loginTimestamp", 0);   // reset time stamp
        editor.apply();
    }
}