package com.example.b07group57;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.click;

import static com.example.b07group57.CalendarFragmentTest.waitForView;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EcoTrackerFragmentTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            activity.setContentView(R.layout.activity_main);

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new EcoTrackerFragment());
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


    @Test
    public void testCategoryToggle() {

        onView(withId(R.id.transportationCategory)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.foodCategory)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.shoppingCategory)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.transportationCategory)).perform(click());
        onView(withId(R.id.transportationSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.transportationCategory)).perform(click());

        onView(withId(R.id.foodCategory)).perform(click());
        onView(withId(R.id.foodSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.foodCategory)).perform(click());

        onView(withId(R.id.shoppingCategory)).perform(click());
        onView(withId(R.id.shoppingSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.shoppingCategory)).perform(click());
    }

}
