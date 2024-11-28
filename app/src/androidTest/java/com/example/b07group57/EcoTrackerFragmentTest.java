package com.example.b07group57;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

import androidx.fragment.app.testing.FragmentScenario;

@RunWith(AndroidJUnit4.class)
public class EcoTrackerFragmentTest {

    @Before
    public void setUp() {
        // EcoTrackerFragmentを直接起動するためにFragmentScenarioを使用
        FragmentScenario.launchInContainer(EcoTrackerFragment.class);
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
        // 各カテゴリーがクリック可能であることを確認
        onView(withId(R.id.transportationCategory)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.foodCategory)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.shoppingCategory)).check(ViewAssertions.matches(isDisplayed()));

        // カテゴリーが画面外にあってもスクロールして表示させる
        onView(withId(R.id.transportationCategory)).perform(click());
        onView(withId(R.id.transportationSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.transportationCategory)).perform(click());
//        onView(withId(R.id.transportationSection)).check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
//
        // 食品カテゴリーをスクロールしてクリックし、セクションの表示/非表示が切り替わることを確認
        onView(withId(R.id.foodCategory)).perform(click());
        onView(withId(R.id.foodSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.foodCategory)).perform(click());
//        onView(withId(R.id.foodSection)).check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // 他のカテゴリー（transportation, shopping）も同様にスクロールしてテスト
        onView(withId(R.id.shoppingCategory)).perform(click());
        onView(withId(R.id.shoppingSection)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        try {
            Thread.sleep(5000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.shoppingCategory)).perform(click());
////        onView(withId(R.id.shoppingSection)).check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

}
