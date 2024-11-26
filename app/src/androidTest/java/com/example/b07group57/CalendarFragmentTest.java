package com.example.b07group57;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CalendarFragmentTest {

    @Before
    public void setUp() {
        // Activityを起動し、Fragmentを読み込む
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            // フラグメントをアクティビティに追加
            activity.setContentView(R.layout.activity_main); // フラグメントのコンテナが含まれていることを確認

            // フラグメントを置き換える
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new CalendarFragment());
            transaction.commit();
        });

        // Fragmentが表示されるまで待機
        Espresso.onView(withId(R.id.fragment_container)).perform(waitForView());
        Espresso.onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    public static ViewAction waitForView() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Wait for the view to be displayed.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                // ビューが表示されるまで待機
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    @Test
    public void testUIUpdatesWhenDateChanged() {
        // CalendarViewが表示されていることを確認
        onView(withId(R.id.calendarView)).check(matches(isDisplayed()));

        // カレンダーの日付をクリックして変更（ここでは例として2024-11-25をクリック）
        onView(withId(R.id.calendarView)).perform(click());

        // TextViewに選択した日付が表示されていることを確認
        onView(withId(R.id.tvSelectedDate)).check(matches(withText("Selected Date: 2024-11-25")));

        // PieChartが表示されていることを確認
        onView(withId(R.id.piechart)).check(matches(isDisplayed()));

        // "Transportation"カテゴリがPieChartに表示されているかを確認
        onView(allOf(withText("Transportation"), isDisplayed())).check(matches(withText("Transportation: 25%")));
    }

    @Test
    public void testPieChartUpdates() {
        // PieChartが表示されていることを確認
        onView(withId(R.id.piechart)).check(matches(isDisplayed()));

        // 仮のデータが正しく反映されているかを確認
        onView(allOf(withText("Transportation"), isDisplayed())).check(matches(withText("Transportation: 25%")));
        onView(allOf(withText("Electricity"), isDisplayed())).check(matches(withText("Electricity: 15%")));
        onView(allOf(withText("Food"), isDisplayed())).check(matches(withText("Food: 10%")));
        onView(allOf(withText("Clothing"), isDisplayed())).check(matches(withText("Clothing: 30%")));
        onView(allOf(withText("Other"), isDisplayed())).check(matches(withText("Other: 20%")));
    }
}
