package com.example.b07group57.utils;

import android.util.Log;

import com.example.b07group57.models.SurveyData;

public class ConsumptionCalculator {

    public static double calculateConsumption(SurveyData data) {

        double total = 0;

        total += calculateBuyClothes(data.buyClothes);
        total = calculateBuySecondHand(data.buySecondHand, total);
        total += calculateBuyElectronics(data.buyElectronics);
        total = calculateRecycle(data.buyClothes, data.buyElectronics, data.recycle, total);

        return total;
    }

    private static double calculateBuyClothes(String response) {

        switch (response) {
            case "Monthly":
                return 360;
            case "Quarterly":
                return 120;
            case "Annually":
                return 100;
            case "Rarely":
                return 5;
        }
        Log.e("calculateBuyClothes","None of the buyClothes data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateBuySecondHand(String response, double currentTotal) {

        switch (response) {
            case "Yes, regularly":
                return currentTotal * 0.5;
            case "Yes, occasionally":
                return currentTotal * 0.7;
            case "No":
                return currentTotal;
        }
        Log.e("calculateBuySecondHand","None of the buySecondHand data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateBuyElectronics(String response) {

        switch (response) {
            case "None":
                return 0;
            case "1":
                return 300;
            case "2":
                return 600;
            case "3":
                return 900;
            case "4 or more":
                return 1200;
        }
        Log.e("calculateBuyElectronics","None of the buyElectronics data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateRecycle(String buyClothes, String buyElectronics, String response, double currentTotal) {

        switch (response) {
            case "Never":
                return currentTotal;
            case "Occasionally":
                return currentTotal - calculateRecycleOccasionally(buyClothes, buyElectronics);
            case "Frequently":
                return currentTotal - calculateRecycleFrequently(buyClothes, buyElectronics);
            case "Always":
                return currentTotal - calculateRecycleAlways(buyClothes, buyElectronics);
        }

        Log.e("calculateRecycle","None of the recycle data matches");
        return Double.NEGATIVE_INFINITY;
    }

    // Returns the reduction
    private static double calculateRecycleOccasionally(String buyClothes, String buyElectronics) {

        double reduction = 0;

        switch (buyClothes) {
            case "Monthly":
            case "Quarterly":
                reduction += 54;
                break;
            case "Annually":
                reduction += 15;
                break;
            case "Rarely":
                reduction += 0.75;
                break;
        }

        switch (buyElectronics) {
            case "None":
                reduction += 0;
                break;
            case "1":
                reduction += 45;
                break;
            case "2":
                reduction += 60;
                break;
            case "3":
                reduction += 90;
                break;
            case "4 or more":
                reduction += 120;
                break;
        }

        return reduction;
    }

    private static double calculateRecycleFrequently(String buyClothes, String buyElectronics) {

        double reduction = 0;

        switch (buyClothes) {
            case "Monthly":
            case "Quarterly":
                reduction += 108;
                break;
            case "Annually":
                reduction += 30;
                break;
            case "Rarely":
                reduction += 1.5;
                break;
        }

        switch (buyElectronics) {
            case "None":
                reduction += 0;
                break;
            case "1":
                reduction += 60;
                break;
            case "2":
                reduction += 120;
                break;
            case "3":
                reduction += 180;
                break;
            case "4 or more":
                reduction += 240;
                break;
        }

        return reduction;
    }

    private static double calculateRecycleAlways(String buyClothes, String buyElectronics) {

        double reduction = 0;

        switch (buyClothes) {
            case "Monthly":
            case "Quarterly":
                reduction += 180;
                break;
            case "Annually":
                reduction += 50;
                break;
            case "Rarely":
                reduction += 2.5;
                break;
        }

        switch (buyElectronics) {
            case "None":
                reduction += 0;
                break;
            case "1":
                reduction += 90;
                break;
            case "2":
                reduction += 180;
                break;
            case "3":
                reduction += 270;
                break;
            case "4 or more":
                reduction += 360;
                break;
        }

        return reduction;
    }
}
