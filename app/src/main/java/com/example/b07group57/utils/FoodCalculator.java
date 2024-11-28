package com.example.b07group57.utils;

import android.util.Log;

import com.example.b07group57.models.SurveyData;

public class FoodCalculator {

    public static double calculateFood(SurveyData data) {

        double total = 0;

        switch (data.diet) {
            case "Vegetarian":
                total += 1000;
                break;
            case "Vegan":
                total += 500;
                break;
            case "Pescatarian (fish/seafood)":
                total += 1500;
                break;
            case "Meat-based (eat all types of animal products)":
                total += calculateMeat(data.beefConsumption, data.porkConsumption,
                        data.fishConsumption, data.chickenConsumption);
                break;
        }

        switch (data.foodWaste) {
            case "Never":
                break;
            case "Rarely":
                total += 23.4;
                break;
            case "Occasionally":
                total += 70.2;
                break;
            case "Frequently":
                total += 140.4;
                break;
        }

        return total;
    }

    private static double calculateMeat(String beefConsumption, String porkConsumption,
                                       String fishConsumption, String chickenConsumption) {

        double total = 0;

        total += calculateBeef(beefConsumption);
        total += calculatePork(porkConsumption);
        total += calculateChicken(chickenConsumption);
        total += calculateFish(fishConsumption);

        return total;
    }

    private static double calculateBeef(String beefConsumption) {

        switch (beefConsumption) {
            case "Daily":
                return 2500;
            case "Frequently (3–5 times/week)":
                return 1900;
            case "Occasionally (1–2 times/week)":
                return 1300;
            case "Never":
                return 0;
        }

        Log.e("calculateBeef","None of the beefConsumption data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculatePork(String porkConsumption) {

        switch (porkConsumption) {
            case "Daily":
                return 1450;
            case "Frequently (3–5 times/week)":
                return 860;
            case "Occasionally (1–2 times/week)":
                return 450;
            case "Never":
                return 0;
        }

        Log.e("calculatePork","None of the porkConsumption data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateChicken(String chickenConsumption) {

        switch (chickenConsumption) {
            case "Daily":
                return 950;
            case "Frequently (3–5 times/week)":
                return 600;
            case "Occasionally (1–2 times/week)":
                return 200;
            case "Never":
                return 0;
        }

        Log.e("calculateChicken","None of the chickenConsumption data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateFish(String fishConsumption) {

        switch (fishConsumption) {
            case "Daily":
                return 800;
            case "Frequently (3–5 times/week)":
                return 500;
            case "Occasionally (1–2 times/week)":
                return 150;
            case "Never":
                return 0;
        }

        Log.e("calculateFish","None of the fishConsumption data matches");
        return Double.NEGATIVE_INFINITY;
    }
}
