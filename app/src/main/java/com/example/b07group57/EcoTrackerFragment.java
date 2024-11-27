package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

public class EcoTrackerFragment extends Fragment {

    public EcoTrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eco_tracker_fragment, container, false);

        // Set up the TextView for each category title
        TextView transportationCategory = view.findViewById(R.id.transportationCategory);
        TextView foodCategory = view.findViewById(R.id.foodCategory);
        TextView shoppingCategory = view.findViewById(R.id.shoppingCategory);

        // Set up the LinearLayouts that will hold the input fields for each category
        LinearLayout transportationSection = view.findViewById(R.id.transportationInputFields);
        LinearLayout foodSection = view.findViewById(R.id.foodInputFields);
        LinearLayout shoppingSection = view.findViewById(R.id.shoppingInputFields);

        // Handle the NestedScrollView
        NestedScrollView scrollView = view.findViewById(R.id.nestedScrollView);

        // Set up listeners for category titles to toggle visibility of sections
        transportationCategory.setOnClickListener(v -> {
            toggleVisibility(transportationSection);
            scrollToView(scrollView, transportationSection);  // Scroll to the section when it becomes visible
        });
        foodCategory.setOnClickListener(v -> {
            toggleVisibility(foodSection);
            scrollToView(scrollView, foodSection);  // Scroll to the section when it becomes visible
        });
        shoppingCategory.setOnClickListener(v -> {
            toggleVisibility(shoppingSection);
            scrollToView(scrollView, shoppingSection);  // Scroll to the section when it becomes visible
        });

        return view;
    }

    // Toggle visibility of a section
    private void toggleVisibility(LinearLayout section) {
        if (section.getVisibility() == View.VISIBLE) {
            section.setVisibility(View.GONE);
        } else {
            section.setVisibility(View.VISIBLE);
        }
    }

    // Scroll to the given view in the NestedScrollView
    private void scrollToView(NestedScrollView scrollView, View view) {
        scrollView.post(() -> {
            // Scroll to the view within the scroll view, ensuring it stays visible within the viewport
            scrollView.smoothScrollTo(0, view.getTop());
        });
    }
}
