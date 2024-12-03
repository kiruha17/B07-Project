package com.example.b07group57;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ACFDisplayPageFragment extends Fragment {

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acf_display_page_fragment, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        TextView resultTextView = view.findViewById(R.id.result_text_view);
        Button nextButton = view.findViewById(R.id.next_button);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve the argument passed to this fragment
        Bundle args = getArguments();
        if (args != null) {
            double annualCarbonFootprint = args.getDouble("annualCarbonFootprint", -1);

            // Display the result
            resultTextView.setText(String.format("Your Annual Carbon Footprint: %.2f tons of CO2", annualCarbonFootprint * 0.0011));

            // Save the annual carbon footprint to the database
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                databaseReference.child("users").child(userId).child("annualCarbonFootprint")
                        .setValue(annualCarbonFootprint + "kg")
                        .addOnSuccessListener(aVoid -> {
                            System.out.println("Database successfully updated with annualCarbonFootprint");
                        })
                        .addOnFailureListener(e -> {
                            System.out.println("Something went wrong when updating the database with annualCarbonFootprint");
                        });
            }
        }

        // Navigate to the MainMenuFragment when "Next" is clicked
        nextButton.setOnClickListener(v -> {
            Fragment mainMenuFragment = new MainMenuFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainMenuFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}