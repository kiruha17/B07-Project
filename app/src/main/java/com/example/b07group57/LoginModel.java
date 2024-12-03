package com.example.b07group57;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginModel implements LoginContract.Model {

    private final FirebaseAuth mAuth;
    private final SharedPreferences sharedPreferences;

    public LoginModel(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isSessionValid() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        long loginTimestamp = sharedPreferences.getLong("loginTimestamp", 0);
        String savedUserId = sharedPreferences.getString("userId", null);
        String savedEmail = sharedPreferences.getString("email", null);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        long currentTime = System.currentTimeMillis();
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;

        // Check if the session is valid and matches the current user's details
        if (isLoggedIn && currentTime - loginTimestamp < thirtyDaysInMillis && currentUser != null) {
            String currentUserId = currentUser.getUid();
            String currentUserEmail = currentUser.getEmail();

            // Ensure the logged-in user matches the stored user data
            return savedUserId != null && savedUserId.equals(currentUserId)
                    && savedEmail != null && savedEmail.equals(currentUserEmail);
        }

        // Session is not valid
        clearLoginState(); // Reset login state if invalid
        return false;
    }

    private void clearLoginState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved login-related data
        editor.apply();
    }

    @Override
    public void authenticate(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null && currentUser.isEmailVerified()) {
                    saveLoginState();
                    callback.onLoginSuccess();
                } else {
                    clearLoginState(); // Clear session state
                    mAuth.signOut(); // Log out the user if their email is not verified
                    callback.onLoginFailure("Email not verified. Please verify your email before logging in.");
                }
            } else {
                String errorMessage = "Unknown error occurred.";
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
                }
                callback.onLoginFailure(errorMessage);
            }
        });
    }

    @Override
    public void checkSurveyDataExists(SurveyCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Check if "surveyData" exists
            userRef.child("surveyData").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // surveyData exists, navigate to main menu
                        callback.onSurveyDataExists();
                    } else {
                        // surveyData missing, navigate to survey
                        callback.onSurveyDataMissing();
                    }
                } else {
                    // Handle any potential errors
                    if (task.getException() != null) {
                        callback.onSurveyCheckError(task.getException().getMessage());
                    } else {
                        callback.onSurveyCheckError("Unknown error occurred while checking surveyData.");
                    }
                }
            });
        } else {
            callback.onSurveyCheckError("User not logged in.");
        }
    }

    private void saveLoginState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putLong("loginTimestamp", System.currentTimeMillis());
            editor.putString("userId", currentUser.getUid());
            editor.putString("email", currentUser.getEmail());
            editor.apply();
        }
    }
}
