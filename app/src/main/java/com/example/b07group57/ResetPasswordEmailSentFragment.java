package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ResetPasswordEmailSentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_email_sent_fragment, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        Button backToLoginButton = view.findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(v -> {
            Fragment loginPageFragment = new LoginPageFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, loginPageFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}