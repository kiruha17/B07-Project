package com.example.b07group57.models;

import android.util.Log;

public class ConsumptionCalculator {

    public static double calculateConsumption(SurveyData data) {

        double total = 0;

        total += calculateBuyClothes(data.buyClothes);
        total = calculateBuySecondHand(data.buySecondHand, total);
        total += calculateBuyElectronics(data.buyElectronics);
        total = calculateRecycle(data.buyClothes, data.buyElectronics, data.recycle, total);

        return total;
    }

    public static double calculateBuyClothes(String response) {

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

    public static double calculateBuySecondHand(String response, double currentTotal) {

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

    public static double calculateBuyElectronics(String response) {

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

    public static double calculateRecycle(String buyClothes, String buyElectronics, String response, double currentTotal) {

        double newTotal;



        Log.e("calculateRecycle","None of the recycle data matches");
        return Double.NEGATIVE_INFINITY;
    }
}
