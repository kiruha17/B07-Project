package com.example.b07group57.models;

//Some emission calculations are from the spreadsheet provided where the CO2e kg for 1-2 uses is divided by 2
public class EcoTrackerEmissionsCalculator {
    public static double calculateEmissions(String activityType, double inputAmount, String unit) {
        switch (activityType) {
            case "Drive Personal Vehicle":
                return getPersonalCarEmissions(unit, inputAmount);
            case "Take Public Transportation":
                return getPublicTransportEmissions(unit, inputAmount);
            case "Cycling or Walking":
                return 0;
            case "Flight":
                if (unit.equals("Short-Haul")) {
                    return 112.5; // Spreadsheet data for 1 Short Haul flight
                }
                else {
                    return inputAmount * 412.5; // Spreadsheet data for 1 Long Haul flight
                }
            case "Meal": //FOOD
                return getMealEmissions(unit, inputAmount);
            case "Buy New Clothes":
                return inputAmount * 5; // Spreadsheet data for 1 clothing purchase, (Rarely)
            case "Buy Electronics":
                return inputAmount * 300; // Spreadsheet data for 1 electronic purchased
            case "Other Purchases":
                return inputAmount * 5;
            case "Energy Bills":
                return getEnergyEmissions(unit, inputAmount);
            default:
                return 0.0;
        }
    }

    private static double getPersonalCarEmissions(String carType, double distance) {
        switch (carType) {
            case "Gasoline":
                return distance * 0.24;
            case "Diesel":
                return distance * 0.27;
            case "Hybrid":
                return distance * 0.16;
            case "Electric":
                return distance * 0.05;
            default:
                return 0.0;
        }
    }

    private static double getPublicTransportEmissions(String publicType, double hours){
        switch (publicType) {
            case "Bus":
                return hours * 5; // Rough rounding of webSource for avg C02e kg/hour
            case "Train":
                return hours * 7; // Rough rounding of webSource for avg C02e kg/dollar
            case "Subway":
                return hours * 3; // Rough rounding of webSource for avg C02e kg/dollar
            default:
                return 0.0;
        }
    }
    private static double getMealEmissions(String mealType, double servings) {
        switch (mealType) {
            case "Beef":
                return servings * 650; // Spreadsheet data for 1 meal
            case "Pork":
                return servings * 225; // Spreadsheet data for 1 meal
            case "Chicken":
                return servings * 100; // Spreadsheet data for 1 meal
            case "Fish":
                return servings * 75; // Spreadsheet data for 1 meal
            default:
                return servings * 20;
        }
    }

    private static double getEnergyEmissions(String billType, double dollars){
        switch (billType) {
            case "Electricity":
                return dollars * 3.5; // Rough rounding of webSource for avg C02e kg/dollar
            case "Gas":
                return dollars * 6; // Rough rounding of webSource for avg C02e kg/dollar
            case "Water":
                return dollars * .1; // Rough rounding of webSource for avg C02e kg/dollar
            default:
                return 0.0;
        }
    }
}
