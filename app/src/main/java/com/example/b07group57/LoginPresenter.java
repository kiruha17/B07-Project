package com.example.b07group57;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;
    private final LoginContract.Model model;

    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void initializeSession() {
        if (model.isSessionValid()) {
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
                model.checkSurveyDataExists(new LoginContract.Model.SurveyCallback() {
                    @Override
                    public void onSurveyDataExists() {
                        view.navigateToMainMenu();
                    }

                    @Override
                    public void onSurveyDataMissing() {
                        view.navigateToSurvey();
                    }

                    @Override
                    public void onSurveyCheckError(String error) {
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
        view.enableLoginButton(email != null && email.length() > 0 && password != null && password.length() > 0);
    }

    @Override
    public void onForgotPasswordButtonClicked() {
        view.navigateToResetPassword();
    }
}
