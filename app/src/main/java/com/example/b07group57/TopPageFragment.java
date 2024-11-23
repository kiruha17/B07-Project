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

public class TopPageFragment extends Fragment {

    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.top_page_fragment, container, false);

        // Find the logout button
        logoutButton = view.findViewById(R.id.logout_button);

        // Set onClickListener for logout
        logoutButton.setOnClickListener(v -> {
            // Perform logout operation (clear login state, etc.)
            loadFragment(new LogoutPageFragment());
        });

        // Set up any other UI elements here (e.g., display user info)

        return view;
    }

    private void loadFragment(Fragment fragment) {
        // FragmentTransaction to replace the current fragment with the new one
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Add to back stack so the user can navigate back
        transaction.commit();
    }
}