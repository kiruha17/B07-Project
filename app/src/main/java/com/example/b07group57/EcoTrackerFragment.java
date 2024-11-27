package com.example.b07group57;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

public class EcoTrackerFragment extends Fragment {
    private EditText driveInput, cyclingWalkingInput, busInput, trainInput, subwayInput, shortFlightInput,
            longFlightInput, beefInput, porkInput, chickenInput, fishInput, plantBasedInput,
            clothingInput, electricityBillsInput, gasBillsInput, waterBillsInput;
    private Button btnEdit, addElectronicDeviceButton, addOtherButton;

    private boolean isEditable = false;
    private List<TextView> deleteTextList = new ArrayList<>();
    private List<TextView> inputTypeTextList = new ArrayList<>();
    private List<TextView> inputTextList = new ArrayList<>();

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

        driveInput = view.findViewById(R.id.driveInput);
        cyclingWalkingInput = view.findViewById(R.id.cyclingWalkingInput);
        busInput = view.findViewById(R.id.busInput);
        trainInput = view.findViewById(R.id.trainInput);
        subwayInput = view.findViewById(R.id.subwayInput);
        shortFlightInput = view.findViewById(R.id.shortFlightInput);
        longFlightInput = view.findViewById(R.id.longFlightInput);
        beefInput = view.findViewById(R.id.beefInput);
        porkInput = view.findViewById(R.id.porkInput);
        chickenInput = view.findViewById(R.id.chickenInput);
        fishInput = view.findViewById(R.id.fishInput);
        plantBasedInput = view.findViewById(R.id.plantBasedInput);
        clothingInput = view.findViewById(R.id.clothingInput);
        electricityBillsInput = view.findViewById(R.id.electricityBillsInput);
        gasBillsInput = view.findViewById(R.id.gasBillsInput);
        waterBillsInput = view.findViewById(R.id.waterBillsInput);


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

        btnEdit = view.findViewById(R.id.btnEdit);
        addElectronicDeviceButton = view.findViewById(R.id.addElectronicDeviceButton);
        addOtherButton = view.findViewById(R.id.addOtherButton);

        // Set up listeners for "Add Field" buttons
        LinearLayout devicePairs = view.findViewById(R.id.devicePairs);
        LinearLayout otherPairs = view.findViewById(R.id.otherPairs);
        addElectronicDeviceButton.setOnClickListener(v -> addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs));
        addOtherButton.setOnClickListener(v -> addNewInput("type (e.g. furniture, appliances)", otherPairs));

        // Edit ボタンのクリックリスナー
        btnEdit.setOnClickListener(v -> {
            if (isEditable) {
                enableEditText(false);
                btnEdit.setText("Edit");
                setAddDeleteButtonsEnabled(false);
            } else {
                enableEditText(true);
                btnEdit.setText("Save");
                setAddDeleteButtonsEnabled(true);
            }
            isEditable = !isEditable;
        });

        addNewInput("type (e.g. smartphone, laptop, TV)", devicePairs);
        addNewInput("type (e.g. furniture, appliances)", otherPairs);
        setAddDeleteButtonsEnabled(isEditable);
        enableEditText(isEditable);

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

    private void addNewInput(String type, LinearLayout parent) {
        LinearLayout newPair = new LinearLayout(getContext());
        newPair.setOrientation(LinearLayout.VERTICAL);

        EditText newTypeInput = new EditText(getContext());
        newTypeInput.setHint(type);
        EditText newInput = new EditText(getContext());
        newInput.setHint("(quantity)");

        TextView deleteText = createDeleteText(getContext(), newPair);

        newPair.addView(deleteText);
        newPair.addView(newTypeInput);
        inputTypeTextList.add(newTypeInput);
        newPair.addView(newInput);
        inputTextList.add(newInput);

        // すでに別の親ビューに追加されている場合、親から削除してから再追加する
        if (newPair.getParent() != null) {
            ((ViewGroup) newPair.getParent()).removeView(newPair);
        }

        parent.addView(newPair);
    }

    public TextView createDeleteText(Context context, ViewGroup parentLayout) {
        // 新しい TextView を作成
        TextView deleteText = new TextView(context);

        // テキスト設定
        deleteText.setText("x");
        deleteText.setTextSize(20);  // テキストサイズを 20sp に設定
        deleteText.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));  // テキストカラーを赤に設定
        deleteText.setGravity(Gravity.CENTER_VERTICAL);  // 縦方向のセンターに配置

        // LayoutParams を設定
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,  // 幅は wrap_content
                ViewGroup.LayoutParams.WRAP_CONTENT);  // 高さも wrap_content に設定
        layoutParams.setMargins(0, 0, 0, 8);  // 右と下にマージン8dpを設定

        // layout_gravity を使用して、親レイアウト内で右端に配置
        layoutParams.gravity = Gravity.END;

        // LayoutParams を TextView に設定
        deleteText.setLayoutParams(layoutParams);

        // クリック可能に設定
        deleteText.setClickable(true);

        // クリックイベントを設定
        deleteText.setOnClickListener(v -> {
            // クリック時の処理（例えば、親レイアウトから削除する）
            deleteTextList.remove(deleteText);  // リストから削除
            deletePair(parentLayout);  // ペアを削除
        });

        // リストに追加
        deleteTextList.add(deleteText);

        return deleteText;
    }



    public void deletePair(ViewGroup parentLayout) {
        if (parentLayout != null && parentLayout.getParent() instanceof ViewGroup) {
            ViewGroup grandParentLayout = (ViewGroup) parentLayout.getParent();
            grandParentLayout.removeView(parentLayout);
        }
    }

    // Scroll to the given view in the NestedScrollView
    private void scrollToView(NestedScrollView scrollView, View view) {
        scrollView.post(() -> {
            // Scroll to the view within the scroll view, ensuring it stays visible within the viewport
            scrollView.smoothScrollTo(0, view.getTop());
        });
    }

    private void enableEditText(boolean isEnabled) {
        driveInput.setFocusable(isEnabled);
        driveInput.setFocusableInTouchMode(isEnabled);
        cyclingWalkingInput.setFocusable(isEnabled);
        cyclingWalkingInput.setFocusableInTouchMode(isEnabled);
        busInput.setFocusable(isEnabled);
        busInput.setFocusableInTouchMode(isEnabled);
        trainInput.setFocusable(isEnabled);
        trainInput.setFocusableInTouchMode(isEnabled);
        subwayInput.setFocusable(isEnabled);
        subwayInput.setFocusableInTouchMode(isEnabled);
        shortFlightInput.setFocusable(isEnabled);
        shortFlightInput.setFocusableInTouchMode(isEnabled);
        longFlightInput.setFocusable(isEnabled);
        longFlightInput.setFocusableInTouchMode(isEnabled);
        beefInput.setFocusable(isEnabled);
        beefInput.setFocusableInTouchMode(isEnabled);
        porkInput.setFocusable(isEnabled);
        porkInput.setFocusableInTouchMode(isEnabled);
        chickenInput.setFocusable(isEnabled);
        chickenInput.setFocusableInTouchMode(isEnabled);
        fishInput.setFocusable(isEnabled);
        fishInput.setFocusableInTouchMode(isEnabled);
        plantBasedInput.setFocusable(isEnabled);
        plantBasedInput.setFocusableInTouchMode(isEnabled);
        clothingInput.setFocusable(isEnabled);
        clothingInput.setFocusableInTouchMode(isEnabled);
        electricityBillsInput.setFocusable(isEnabled);
        electricityBillsInput.setFocusableInTouchMode(isEnabled);
        gasBillsInput.setFocusable(isEnabled);
        gasBillsInput.setFocusableInTouchMode(isEnabled);
        waterBillsInput.setFocusable(isEnabled);
        waterBillsInput.setFocusableInTouchMode(isEnabled);
        for (TextView inputTypeText : inputTypeTextList) {
            inputTypeText.setFocusable(isEnabled);
            inputTypeText.setFocusableInTouchMode(isEnabled);
        }
        for (TextView inputText : inputTextList) {
            inputText.setFocusable(isEnabled);
            inputText.setFocusableInTouchMode(isEnabled);
        }
    }

    private void setAddDeleteButtonsEnabled(boolean isEnabled) {
        for (TextView deleteText : deleteTextList) {
            deleteText.setClickable(isEnabled);
            deleteText.setFocusable(isEnabled);
            deleteText.setTextColor(isEnabled
                    ? getResources().getColor(android.R.color.holo_red_dark)
                    : getResources().getColor(android.R.color.darker_gray));
        }

        addElectronicDeviceButton.setEnabled(isEnabled);
        addOtherButton.setEnabled(isEnabled);
    }
}
