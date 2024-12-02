package com.example.b07group57;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;
    private final LoginContract.Model model;

    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        this.view = view;
        this.model = model;

        // Check session state
        if (model.isSessionValid()) {
            view.showToast("Session valid, navigating...");
            view.navigateToMainMenu();
        } else {
            view.showToast("Please log in.");
        }
    }

    @Override
    public void onLoginButtonClicked(String email, String password) {
        model.authenticate(email, password, new LoginContract.Model.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                // After successful login, check survey data
                model.checkSurveyDataExists(new LoginContract.Model.SurveyCallback() {
                    @Override
                    public void onSurveyDataExists() {
                        // Navigate to main menu
                        view.navigateToMainMenu();
                    }

                    @Override
                    public void onSurveyDataMissing() {
                        // Navigate to survey page
                        view.navigateToSurvey();
                    }

                    @Override
                    public void onSurveyCheckError(String error) {
                        // Handle any errors during survey data check
                        view.showToast("Error checking survey data: " + error);
                    }
                });
            }

            @Override
            public void onLoginFailure(String message) {
                view.showToast(message);
            }
        });
    }

    @Override
    public void onEmailOrPasswordChanged(CharSequence email, CharSequence password) {
        boolean isEmailFilled = email != null && email.length() > 0;
        boolean isPasswordFilled = password != null && password.length() > 0;
        view.enableLoginButton(isEmailFilled && isPasswordFilled);
    }

    @Override
    public void onForgotPasswordButtonClicked() {
        view.navigateToResetPassword();
    }
}
