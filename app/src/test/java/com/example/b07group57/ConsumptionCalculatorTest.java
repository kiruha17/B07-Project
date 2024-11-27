package com.example.b07group57;


import org.junit.Test;
import static org.junit.Assert.*;

import com.example.b07group57.utils.ConsumptionCalculator;
import com.example.b07group57.models.SurveyData;

public class ConsumptionCalculatorTest {

    @Test
    public void testConsumptionCalculator1() {
        SurveyData surveyData = new SurveyData("", "Monthly", "2", "No", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "Never",
                "", "", "", "");

        assertEquals(960.0, ConsumptionCalculator.calculateConsumption(surveyData), 0);
    }

    @Test
    public void testConsumptionCalculator2() {
        SurveyData surveyData2 = new SurveyData("", "Quarterly", "3", "Yes, occasionally", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "Frequently",
                "", "", "", "");


        assertEquals(696.0, ConsumptionCalculator.calculateConsumption(surveyData2), 0);
    }
}
