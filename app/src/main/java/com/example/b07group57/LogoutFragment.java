package com.example.b07group57;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;

    public class LogoutFragment extends Fragment {
        private FirebaseAuth mAuth;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAuth = FirebaseAuth.getInstance();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // when logout button is clicked
            Button logoutButton = getView().findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
        }

        private void showLogoutConfirmationDialog() {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> logout())
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        private void logout() {
            // sign out from Firebase
            mAuth.signOut();

            // reset SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.putLong("loginTimestamp", 0);
            editor.apply();

            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // move to login page
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

