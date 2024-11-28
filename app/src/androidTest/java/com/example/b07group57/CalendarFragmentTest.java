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
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            activity.setContentView(R.layout.activity_main);

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new CalendarFragment());
            transaction.commit();
        });

        Espresso.onView(withId(R.id.fragment_container)).perform(waitForView());
        Espresso.onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testInteraction() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    @Test
    public void testUIUpdatesWhenDateChanged() {
        onView(withId(R.id.calendarView)).check(matches(isDisplayed()));
        onView(withId(R.id.calendarView)).perform(click());
        onView(withId(R.id.tvSelectedDate)).check(matches(withText("Selected Date: 2024-11-25")));
        onView(withId(R.id.piechart)).check(matches(isDisplayed()));
        onView(allOf(withText("Transportation"), isDisplayed())).check(matches(withText("Transportation: 25%")));
    }

    @Test
    public void testPieChartUpdates() {
        onView(withId(R.id.piechart)).check(matches(isDisplayed()));
        onView(allOf(withText("Transportation"), isDisplayed())).check(matches(withText("Transportation: 25%")));
        onView(allOf(withText("Electricity"), isDisplayed())).check(matches(withText("Electricity: 15%")));
        onView(allOf(withText("Food"), isDisplayed())).check(matches(withText("Food: 10%")));
        onView(allOf(withText("Clothing"), isDisplayed())).check(matches(withText("Clothing: 30%")));
        onView(allOf(withText("Other"), isDisplayed())).check(matches(withText("Other: 20%")));
    }
}
