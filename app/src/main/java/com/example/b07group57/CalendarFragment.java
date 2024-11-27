package com.example.b07group57;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class CalendarFragment extends Fragment {

    private DatabaseReference mDatabase;
    private PieChart pieChart;
    private TextView tvSelectedDate, tvActivityDetails;
    private Button btnDetails;

    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebaseのインスタンスを取得
        // mDatabase = FirebaseDatabase.getInstance().getReference("CO2Emission");

        // UIの参照を取得
        pieChart = view.findViewById(R.id.piechart);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvActivityDetails = view.findViewById(R.id.tvActivityDetails);
        btnDetails = view.findViewById(R.id.btnDetails);

        // カレンダーの選択日付を取得
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        long currentDate = System.currentTimeMillis();

        String selectedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(currentDate));
        tvSelectedDate.setText("Selected Date: " + selectedDate);

        calendarView.setDate(currentDate, true, true);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String changedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            tvSelectedDate.setText("Selected Date: " + changedDate);
            loadEmissionData(changedDate);
        });

        loadEmissionData(selectedDate);
    }


    private void loadEmissionData(String selectedDate) {
//        mDatabase.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                float transportation = 0, electricity = 0, food = 0, clothing = 0, other = 0;
//
//                // データが存在する場合に値を取得
//                if (dataSnapshot.child("Transportation").exists()) {
//                    transportation = dataSnapshot.child("Transportation").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Electricity").exists()) {
//                    electricity = dataSnapshot.child("Electricity").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Food").exists()) {
//                    food = dataSnapshot.child("Food").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Clothing").exists()) {
//                    clothing = dataSnapshot.child("Clothing").getValue(Float.class);
//                }
//                if (dataSnapshot.child("Other").exists()) {
//                    other = dataSnapshot.child("Other").getValue(Float.class);
//                }

                float transportation = 25, electricity = 15, food = 10, clothing = 30, other = 20;

                // PieChartにデータを追加
                pieChart.clearChart(); // 既存データをクリア
                pieChart.addPieSlice(new PieModel("Transportation", transportation, Color.parseColor("#FFA726")));
                pieChart.addPieSlice(new PieModel("Electricity", electricity, Color.parseColor("#66BB6A")));
                pieChart.addPieSlice(new PieModel("Food", food, Color.parseColor("#EF5350")));
                pieChart.addPieSlice(new PieModel("Clothing", clothing, Color.parseColor("#29B6F6")));
                pieChart.addPieSlice(new PieModel("Other", other, Color.parseColor("#FFEB3B")));

                // アニメーションを開始
                pieChart.startAnimation();

                // 横のラベルと値を更新
                updateLabels(transportation, electricity, food, clothing, other);

                // Firebaseから活動データを取得して表示する処理を追加
                // 仮のデータ
                tvActivityDetails.setText("Activity data for " + selectedDate);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FirebaseError", "Failed: " + databaseError.getMessage());
//                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // 横のラベルと値を更新
    private void updateLabels(float transportation, float electricity, float food, float clothing, float other) {
        LinearLayout labelContainer = getView().findViewById(R.id.labelContainer);

        // 現在のレイアウトの子ビューをクリア
        labelContainer.removeAllViews();

        // ラベルと値を追加
        addLabel(labelContainer, "Transportation", transportation, Color.parseColor("#FFA726"));
        addLabel(labelContainer, "Electricity", electricity, Color.parseColor("#66BB6A"));
        addLabel(labelContainer, "Food", food, Color.parseColor("#EF5350"));
        addLabel(labelContainer, "Clothing", clothing, Color.parseColor("#29B6F6"));
        addLabel(labelContainer, "Other", other, Color.parseColor("#FFEB3B"));
    }

    // ラベルを追加するメソッド
    private void addLabel(LinearLayout container, String category, float value, int color) {
        LinearLayout labelLayout = new LinearLayout(getContext());
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);
        labelLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // 色付きの小さなサンプルビュー
        View colorView = new View(getContext());
        colorView.setLayoutParams(new LinearLayout.LayoutParams(15, 15));
        colorView.setBackgroundColor(color);

        // テキストビューにカテゴリ名と値を表示
        TextView textView = new TextView(getContext());
        textView.setText(category + ": " + value + "%");
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding(10, 0, 0, 0);

        // 追加
        labelLayout.addView(colorView);
        labelLayout.addView(textView);
        container.addView(labelLayout);
    }
}
