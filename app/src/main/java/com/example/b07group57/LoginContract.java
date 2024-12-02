package com.example.b07group57;

public interface LoginContract {

    interface View {
        void enableLoginButton(boolean enable);
        void showToast(String message);
        void navigateToMainMenu();
        void navigateToSurvey();
        void navigateToResetPassword();
    }

    interface Presenter {
        void onLoginButtonClicked(String email, String password);
        void onEmailOrPasswordChanged(CharSequence email, CharSequence password);
        void onForgotPasswordButtonClicked();
    }

    interface Model {
        boolean isSessionValid();
        void authenticate(String email, String password, LoginCallback callback);
        void checkSurveyDataExists(SurveyCallback callback);

        interface SurveyCallback {
            void onSurveyDataExists();
            void onSurveyDataMissing();
            void onSurveyCheckError(String error);
        }

        interface LoginCallback {
            void onLoginSuccess();
            void onLoginFailure(String message);
        }
    }
}
