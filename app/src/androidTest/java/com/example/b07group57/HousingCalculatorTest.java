package com.example.b07group57;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.b07group57.models.SurveyData;
import com.example.b07group57.utils.HousingCalculator;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HousingCalculatorTest {

    @Test
    public void testHousingCalculator1() {

        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        SurveyData surveyData = new SurveyData("", "",
                "", "", "", "",
                "", "", "",
                "", "$100-$150",
                "", "", "Natural Gas",
                "2", "1000–2000 sq. ft.",
                "Townhouse", "", "", "", "",
                "No", "", "", "Natural Gas");

        assertEquals(5500.0, HousingCalculator.calculateHousing(context, surveyData), 0);
    }
}