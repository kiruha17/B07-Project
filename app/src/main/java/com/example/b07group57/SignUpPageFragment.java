package com.example.b07group57;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPageFragment extends Fragment {

    private EditText emailTextView, passwordTextView, conPasswordTextView, nameTextView;
    private Button regBtn;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_page_fragment, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        
        emailTextView = view.findViewById(R.id.email_input);
        passwordTextView = view.findViewById(R.id.password_input);
        conPasswordTextView = view.findViewById(R.id.conpassword);
        nameTextView = view.findViewById(R.id.user_name);
        regBtn = view.findViewById(R.id.register);

        // Add a TextWatcher to enable the button dynamically
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isAllFieldsFilled = !TextUtils.isEmpty(emailTextView.getText().toString()) &&
                        !TextUtils.isEmpty(passwordTextView.getText().toString()) &&
                        !TextUtils.isEmpty(conPasswordTextView.getText().toString()) &&
                        !TextUtils.isEmpty(nameTextView.getText().toString());

                Log.d("SignUpPageFragment", "All fields filled: " + isAllFieldsFilled);
                regBtn.setEnabled(isAllFieldsFilled);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Attach the TextWatcher to all input fields
        emailTextView.addTextChangedListener(textWatcher);
        passwordTextView.addTextChangedListener(textWatcher);
        conPasswordTextView.addTextChangedListener(textWatcher);
        nameTextView.addTextChangedListener(textWatcher);

        // Register button click listener
        regBtn.setOnClickListener(v -> {
            Log.d("SignUpPageFragment", "Register button clicked!");
            mAuth.signOut();
            registerNewUser();
        });

        return view;
    }

    private void registerNewUser() {
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        String conPassword = conPasswordTextView.getText().toString();
        String name = nameTextView.getText().toString();

        // Validations for input fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter an email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(conPassword)) {
            Toast.makeText(getContext(), "Please confirm your password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Please enter your name!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(conPassword)) {
            Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Firebase authentication logic
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),
                                                        "Registration successful. Please verify your email.",
                                                        Toast.LENGTH_LONG).show();
                                                navigateToLoginFragment();
                                            } else {
                                                Toast.makeText(getContext(),
                                                        "Failed to send verification email. Please try again.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(),
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void navigateToLoginFragment() {
        // Navigate to LoginFragment after successful registration
        Fragment loginFragment = new LoginPageFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();
    }
}