package com.example.b07group57;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class LoginPageFragment extends Fragment implements LoginContract.View {

    private EditText emailField, passwordField;
    private Button loginButton, forgotPasswordButton;
    private LoginContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page_fragment, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        // Initialize views
        emailField = view.findViewById(R.id.email);
        passwordField = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        forgotPasswordButton = view.findViewById(R.id.forgot_password_button);

        presenter = new LoginPresenter(this, new LoginModel(requireContext()));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onEmailOrPasswordChanged(emailField.getText(), passwordField.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        emailField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> presenter.onLoginButtonClicked(
                emailField.getText().toString().trim(),
                passwordField.getText().toString().trim()
        ));

        forgotPasswordButton.setOnClickListener(v -> presenter.onForgotPasswordButtonClicked());

        return view;
    }

    @Override
    public void enableLoginButton(boolean enable) {
        loginButton.setEnabled(enable);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainMenu() {
        loadFragment(new MainMenuFragment());
    }

    @Override
    public void navigateToSurvey() {
        loadFragment(new AnnualCarbonFootprintSurveyFragment());
    }

    @Override
    public void navigateToResetPassword() {
        loadFragment(new ResetPasswordFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
