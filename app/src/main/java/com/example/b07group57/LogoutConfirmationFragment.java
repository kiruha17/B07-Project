package com.example.b07group57;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LogoutConfirmationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_confirmation_fragment, container, false);

        // Start a timer to navigate back to the home page after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToHomePage, 3000); // 3 seconds

        return view;
    }

    private void navigateToHomePage() {
        Fragment homePageFragment = new HomePageFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, homePageFragment);
        transaction.addToBackStack(null); // Add to back stack if needed
        transaction.commit();
    }
}