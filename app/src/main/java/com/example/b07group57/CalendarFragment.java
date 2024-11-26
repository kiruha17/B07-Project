import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class CalendarFragment extends Fragment {

    private DatePicker datePicker;
    private EditText activityLog;
    private Button saveActivityButton;
    private TextView selectedDateTextView;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        datePicker = view.findViewById(R.id.date_picker);
        activityLog = view.findViewById(R.id.activity_log);
        saveActivityButton = view.findViewById(R.id.save_activity_button);
        selectedDateTextView = view.findViewById(R.id.selected_date);

        // Set up the DatePicker listener
        datePicker.init(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    selectedDateTextView.setText("Selected Date: " + selectedDate);
                });

        // Set up the save button listener
        saveActivityButton.setOnClickListener(v -> saveActivityToDatabase());

        return view;
    }

    // Save the activity to Firestore
    private void saveActivityToDatabase() {
        String activity = activityLog.getText().toString().trim();
        if (activity.isEmpty()) {
            Toast.makeText(getContext(), "Please enter activity details", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // Firestore stores months as 1-indexed
        int day = datePicker.getDayOfMonth();

        // Format the date as "YYYY-MM-DD"
        String selectedDate = year + "-" + month + "-" + day;

        // Create a new activity log object
        ActivityLog newActivity = new ActivityLog(selectedDate, activity);

        // Save the activity to Firestore
        db.collection("activity_logs")
                .document(selectedDate)
                .set(newActivity)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Activity saved successfully", Toast.LENGTH_SHORT).show();
                    activityLog.setText(""); // Clear the input field after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save activity", Toast.LENGTH_SHORT).show();
                });
    }

    // ActivityLog class to represent the data structure for Firestore
    public static class ActivityLog {
        private String date;
        private String activity;

        public ActivityLog(String date, String activity) {
            this.date = date;
            this.activity = activity;
        }

        public String getDate() {
            return date;
        }

        public String getActivity() {
            return activity;
        }
    }
}
