package com.example.b07group57;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.b07group57.models.SurveyData;
import com.example.b07group57.utils.TransportationCalculator;

import org.junit.Test;

public class TransportCalculatorTest {

    @Test
    public void TransportCalculatorTest1() {

        SurveyData surveyData = new SurveyData("", "", "",
                "", "Yes", "Hybrid",
                "", "", "",
                "20,000–25,000 km (12,000–15,000 miles)", "",
                "", "", "", "", "",
                "", "1–2 flights", "",
                "Occasionally (1 to 2 times per week)", "",
                "", "1–2 flights",
                "3–5 hours", "");

        assertEquals(6688.0, TransportationCalculator.calculateTransportation(surveyData),
                0);
    }

    @Test
    public void TransportCalculatorTest2() {

        SurveyData surveyData = new SurveyData("", "", "",
                "", "No", "Hybrid",
                "", "", "",
                "20,000–25,000 km (12,000–15,000 miles)", "",
                "", "", "", "", "",
                "", "1–2 flights", "",
                "Always (5 or more times per week)", "",
                "", "6–10 flights",
                "More than 10 hours", "");

        assertEquals(11580.0, TransportationCalculator.calculateTransportation(surveyData),
                0);
    }
}
