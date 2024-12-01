package com.example.b07group57.utils;

import com.example.b07group57.models.SurveyData;

import java.io.InputStream;

public class ACFCalculator {

    public static double calculateAnnualCarbonFootprint(InputStream housingDataExcel, SurveyData surveyData) {
        double transportationCO2 = TransportationCalculator.calculateTransportation(surveyData);
        double foodCO2 = FoodCalculator.calculateFood(surveyData);
        double consumptionCO2 = ConsumptionCalculator.calculateConsumption(surveyData);
        double housingCO2 = HousingCalculator.calculateHousing(housingDataExcel, surveyData);

        return transportationCO2 + foodCO2 + consumptionCO2 + housingCO2;
    }
}