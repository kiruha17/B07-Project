package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.b07group57.models.ACFResult;
import com.example.b07group57.utils.ACFCalculator;
import com.example.b07group57.models.SurveyData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class ACFLoadingPageFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acf_loading_page_fragment, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        fetchSurveyDataAndCalculate(view);

        return view;
    }

    private void fetchSurveyDataAndCalculate(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference.child("users").child(userId).child("surveyData")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot != null) {
                            SurveyData surveyData = snapshot.getValue(SurveyData.class);
                            if (surveyData != null) {
                                try {
                                    InputStream housingDataExcel = requireContext().getAssets().open("HousingData.xlsx");
                                    ACFResult result = ACFCalculator.calculateAnnualCarbonFootprint(housingDataExcel, surveyData);

                                    // Pass the result to the next fragment
                                    Fragment acfDisplayPageFragment = new ACFDisplayPageFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putDouble("total", result.total);
                                    bundle.putDouble("transportation", result.transportation);
                                    bundle.putDouble("food", result.food);
                                    bundle.putDouble("housing", result.housing);
                                    bundle.putDouble("consumption", result.consumption);
                                    bundle.putString("country", surveyData.country);
                                    acfDisplayPageFragment.setArguments(bundle);

                                    // Navigate to the display page
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, acfDisplayPageFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), "Error calculating carbon footprint: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Survey data is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch survey data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}