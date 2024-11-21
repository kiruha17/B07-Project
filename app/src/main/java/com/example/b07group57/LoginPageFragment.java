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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginPageFragment extends Fragment {

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

        // Set the login button click listener
        loginButton.setOnClickListener(v -> {
            navigateToLogoutPage();
        });

        forgotPasswordButton.setOnClickListener(v -> {
            navigateToResetPasswordPage();
        });

        return view;
    }

    private void navigateToLogoutPage() {
        // Navigate to LogoutPageFragment
        Fragment logoutFragment = new LogoutPageFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, logoutFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToResetPasswordPage() {
        // Navigate to ResetPasswordEmailInputFragment
        Fragment resetPasswordFragment = new ResetPasswordFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resetPasswordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}