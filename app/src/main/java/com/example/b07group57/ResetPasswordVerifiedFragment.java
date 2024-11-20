package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.mindrot.jbcrypt.BCrypt;

public class ResetPasswordVerifiedFragment extends Fragment{
    private String userEmail;
    DatabaseReference database = FirebaseDatabase
            .getInstance("https://b07-group-57-default-rtdb.firebaseio.com/").getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password_verified, container, false);
        EditText newPassword = view.findViewById(R.id.newPassword);
        EditText confirmPassword = view.findViewById(R.id.confirmPassword);
        Button submitPassword = view.findViewById(R.id.submitPasswordButton);

        if (getArguments() != null) {
            userEmail = getArguments().getString("user_email");
        }

        submitPassword.setOnClickListener(v -> {
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();
            updatePassword(userEmail, newPass, confirmPass);
        });
        return view;
    }
    public void updatePassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            //display message
            return;
        }
        DatabaseReference userRef = database.child(email);
        userRef.setValue(hashPassword(newPassword));
    }
    public String hashPassword(String unhashedPassword) {
        return BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(10));
    }
}