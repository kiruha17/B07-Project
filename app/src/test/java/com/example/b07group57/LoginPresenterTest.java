package com.example.b07group57;

import static org.mockito.Mockito.*;

import com.example.b07group57.LoginContract;
import com.example.b07group57.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginPresenterTest {

    @Mock
    private LoginContract.View mockView;

    @Mock
    private LoginContract.Model mockModel;

    private LoginPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor_validSession() {

        when(mockModel.isSessionValid()).thenReturn(true);

        presenter = new LoginPresenter(mockView, mockModel);

        verify(mockView).showToast("Session valid, navigating...");
        verify(mockView).navigateToMainMenu();
    }

    @Test
    public void testConstructor_invalidSession() {

        when(mockModel.isSessionValid()).thenReturn(false);

        presenter = new LoginPresenter(mockView, mockModel);

        verify(mockView).showToast("Please log in.");
    }

    @Test
    public void testOnLoginButtonClicked_successWithSurveyData() {

        presenter = new LoginPresenter(mockView, mockModel);

        doAnswer(invocation -> {
            LoginContract.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onLoginSuccess();
            return null;
        }).when(mockModel).authenticate(eq("email@example.com"), eq("password"), any());

        doAnswer(invocation -> {
            LoginContract.Model.SurveyCallback callback = invocation.getArgument(0);
            callback.onSurveyDataExists();
            return null;
        }).when(mockModel).checkSurveyDataExists(any());

        presenter.onLoginButtonClicked("email@example.com", "password");

        verify(mockView).navigateToMainMenu();
    }

    @Test
    public void testOnLoginButtonClicked_successWithoutSurveyData() {

        presenter = new LoginPresenter(mockView, mockModel);

        doAnswer(invocation -> {
            LoginContract.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onLoginSuccess();
            return null;
        }).when(mockModel).authenticate(eq("email@example.com"), eq("password"), any());

        doAnswer(invocation -> {
            LoginContract.Model.SurveyCallback callback = invocation.getArgument(0);
            callback.onSurveyDataMissing();
            return null;
        }).when(mockModel).checkSurveyDataExists(any());

        presenter.onLoginButtonClicked("email@example.com", "password");

        verify(mockView).navigateToSurvey();
    }

    @Test
    public void testOnLoginButtonClicked_failure() {

        presenter = new LoginPresenter(mockView, mockModel);

        doAnswer(invocation -> {
            LoginContract.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onLoginFailure("Invalid credentials");
            return null;
        }).when(mockModel).authenticate(eq("email@example.com"), eq("password"), any());

        presenter.onLoginButtonClicked("email@example.com", "password");

        verify(mockView).showToast("Invalid credentials");
    }

    @Test
    public void testOnLoginButtonClicked_surveyDataCheckError() {

        presenter = new LoginPresenter(mockView, mockModel);

        doAnswer(invocation -> {
            LoginContract.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onLoginSuccess();
            return null;
        }).when(mockModel).authenticate(eq("email@example.com"), eq("password"), any());

        doAnswer(invocation -> {
            LoginContract.Model.SurveyCallback callback = invocation.getArgument(0);
            callback.onSurveyCheckError("Database error");
            return null;
        }).when(mockModel).checkSurveyDataExists(any());

        presenter.onLoginButtonClicked("email@example.com", "password");

        verify(mockView).showToast("Error checking survey data: Database error");
    }

    @Test
    public void testOnEmailOrPasswordChanged_validInput() {

        presenter = new LoginPresenter(mockView, mockModel);

        presenter.onEmailOrPasswordChanged("email@example.com", "password");

        verify(mockView).enableLoginButton(true);
    }

    @Test
    public void testOnEmailOrPasswordChanged_invalidInput() {

        presenter = new LoginPresenter(mockView, mockModel);

        presenter.onEmailOrPasswordChanged("", "");

        verify(mockView).enableLoginButton(false);
    }

    @Test
    public void testOnForgotPasswordButtonClicked() {

        presenter = new LoginPresenter(mockView, mockModel);

        presenter.onForgotPasswordButtonClicked();

        verify(mockView).navigateToResetPassword();
    }
}