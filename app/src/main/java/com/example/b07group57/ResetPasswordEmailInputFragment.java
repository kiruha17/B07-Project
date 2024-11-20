package com.example.b07group57;


import android.os.Bundle;
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

public class ResetPasswordEmailInputFragment extends Fragment {
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password_email_input, container, false);
        auth = FirebaseAuth.getInstance();

        EditText email = view.findViewById(R.id.emailEditText);
        Button submitEmailButton = view.findViewById(R.id.sendEmailButton);

        submitEmailButton.setOnClickListener(v -> {
            String emailAddress = email.getText().toString().trim();
            sendPasswordResetEmail(emailAddress);
        });

        return view;
    }

    private void sendPasswordResetEmail(String email) {
        //Send email logic
        /*
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ResetPasswordEmailVerifiedFragment emailSentFragment = new ResetPasswordEmailVerifiedFragment();

                        Bundle args = new Bundle();
                        args.putString("user_email", email);
                        emailSentFragment.setArguments(args);

                        // Navigate to the next fragment
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, emailSentFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getActivity(), "Error sending email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        */
    }
}