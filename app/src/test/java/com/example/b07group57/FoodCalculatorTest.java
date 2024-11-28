package com.example.b07group57;


import org.junit.Test;
import static org.junit.Assert.*;

import com.example.b07group57.utils.FoodCalculator;
import com.example.b07group57.models.SurveyData;

public class FoodCalculatorTest {

    @Test
    public void testFoodCalculator1() {

        SurveyData surveyData = new SurveyData("Daily", "",
                "", "", "", "",
                "Daily", "", "Meat-based (eat all types of animal products)",
                "", "",
                "Daily", "Occasionally", "", "", "",
                "", "", "Daily", "", "",
                "", "", "", "");


        assertEquals(5770.2, FoodCalculator.calculateFood(surveyData), 0);
    }

    @Test
    public void testFoodCalculator2() {

        SurveyData surveyData = new SurveyData("", "",
                "", "", "", "",
                "", "", "Pescatarian (fish/seafood)",
                "", "",
                "", "Never", "", "", "",
                "", "", "", "", "",
                "", "", "", "");

        assertEquals(1500.0, FoodCalculator.calculateFood(surveyData), 0);
    }
}
