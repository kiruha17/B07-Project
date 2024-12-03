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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new LoginPresenter(mockView, mockModel);
    }

    @Test
    public void testInitializeSession_validSession() {
        when(mockModel.isSessionValid()).thenReturn(true);

        presenter.initializeSession();

        verify(mockView).navigateToMainMenu();
        verify(mockView, never()).showToast(anyString());
    }

    @Test
    public void testInitializeSession_invalidSession() {
        when(mockModel.isSessionValid()).thenReturn(false);

        presenter.initializeSession();

        verify(mockView).showToast("Please log in.");
        verify(mockView, never()).navigateToMainMenu();
    }

    @Test
    public void testOnLoginButtonClicked_successSurveyExists() {
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
    public void testOnLoginButtonClicked_successSurveyMissing() {
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
        doAnswer(invocation -> {
            LoginContract.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onLoginFailure("Invalid credentials");
            return null;
        }).when(mockModel).authenticate(eq("email@example.com"), eq("password"), any());

        presenter.onLoginButtonClicked("email@example.com", "password");

        verify(mockView).showToast("Invalid credentials");
    }

    @Test
    public void testOnForgotPasswordButtonClicked() {
        presenter.onForgotPasswordButtonClicked();

        verify(mockView).navigateToResetPassword();
    }

    @Test
    public void testOnEmailOrPasswordChanged_validInput() {
        presenter.onEmailOrPasswordChanged("email@example.com", "password");

        verify(mockView).enableLoginButton(true);
    }

    @Test
    public void testOnEmailOrPasswordChanged_invalidInput() {
        presenter.onEmailOrPasswordChanged("", "");

        verify(mockView).enableLoginButton(false);
    }
}