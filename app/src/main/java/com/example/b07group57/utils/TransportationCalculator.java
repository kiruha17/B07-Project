package com.example.b07group57.utils;

import android.util.Log;

import com.example.b07group57.models.SurveyData;

public class TransportationCalculator {

    public static double calculateTransportation(SurveyData data) {

        double total = 0;

        if (data.carOwner.equals("Yes")) {
            total += calculateCarEmissions(data.carType, data.distanceDriven);
        }

        if (!data.publicTransportFreq.equals("Never")) {
            total += calculatePublicTransportEmissions(data.publicTransportFreq,
                    data.timeOnPublicTransport);
        }

        total += calculateFlightEmissions(data.shortHaulFlights, data.longHaulFlights);

        return total;
    }

    private static double calculateCarEmissions(String carType, String distanceDriven) {

        double distance = distanceSetter(distanceDriven);

        switch (carType) {
            case "Gasoline":
            case "Unsure":
                return 0.24 * distance;
            case "Diesel":
                return 0.27 * distance;
            case "Hybrid":
                return 0.16 * distance;
            case "Electric":
                return 0.05 * distance;
        }
        Log.e("calculateCarEmissions","None of the carType data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double distanceSetter(String distanceDriven) {

        switch (distanceDriven) {
            case "Up to 5,000 km (3,000 miles)":
                return 5000;
            case "5,000–10,000 km (3,000–6,000 miles)":
                return 10000;
            case "10,000–15,000 km (6,000–9,000 miles)":
                return 15000;
            case "15,000–20,000 km (9,000–12,000 miles)":
                return 20000;
            case "20,000–25,000 km (12,000–15,000 miles)":
                return 25000;
            case "More than 25,000 km (15,000 miles)":
                return 35000;
        }
        Log.e("distanceSetter","None of the distanceDriven data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculatePublicTransportEmissions(String publicTransportFreq,
                                                            String timeOnPublicTransport) {

        switch (publicTransportFreq) {
            case "Occasionally (1 to 2 times per week)":
                return calculatePTEOccasionally(timeOnPublicTransport);
            case "Frequently (3 to 4 times per week)":
                return calculatePTEFrequently(timeOnPublicTransport);
            case "Always (5 or more times per week)":
                return calculatePTEAlways(timeOnPublicTransport);
        }
        Log.e("calculatePublicTransportEmissions",
                "None of the publicTransportFreq data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculatePTEOccasionally(String timeOnPublicTransport) {

        switch (timeOnPublicTransport) {
            case "Under 1 hour":
                return 246;
            case "1–3 hours":
                return 819;
            case "3–5 hours":
                return 1638;
            case "5–10 hours":
                return 3071;
            case "More than 10 hours":
                return 4095;
        }
        Log.e("calculatePTEOccasionally",
                "None of the timeOnPublicTransport data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculatePTEFrequently(String timeOnPublicTransport) {

        switch (timeOnPublicTransport) {
            case "Under 1 hour":
                return 573;
            case "1–3 hours":
                return 1911;
            case "3–5 hours":
                return 3822;
            case "5–10 hours":
                return 7166;
            case "More than 10 hours":
                return 9555;
        }
        Log.e("calculatePTEFrequently",
                "None of the timeOnPublicTransport data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculatePTEAlways(String timeOnPublicTransport) {

        switch (timeOnPublicTransport) {
            case "Under 1 hour":
                return 573;
            case "1–3 hours":
                return 1911;
            case "3–5 hours":
                return 3822;
            case "5–10 hours":
                return 7166;
            case "More than 10 hours":
                return 9555;
        }
        Log.e("calculatePTEAlways",
                "None of the timeOnPublicTransport data matches");
        return Double.NEGATIVE_INFINITY;
    }

    private static double calculateFlightEmissions(String shortHaulFlights,
                                                   String longHaulFlights) {

        double total = 0;

        switch (shortHaulFlights) {
            case "None":
                total += 0;
                break;
            case "1–2 flights":
                total += 225;
                break;
            case "3–5 flights":
                total += 600;
                break;
            case "6–10 flights":
                total += 1200;
                break;
            case "More than 10 flights":
                total += 1800;
                break;
            default:
                Log.e("calculateFlightEmissions",
                        "None of the shortHaulFlights data matches");
                return Double.NEGATIVE_INFINITY;
        }
        switch (longHaulFlights) {
            case "None":
                total += 0;
                break;
            case "1–2 flights":
                total += 825;
                break;
            case "3–5 flights":
                total += 2200;
                break;
            case "6–10 flights":
                total += 4400;
                break;
            case "More than 10 flights":
                total += 6600;
                break;
            default:
                Log.e("calculateFlightEmissions",
                        "None of the shortHaulFlights data matches");
                return Double.NEGATIVE_INFINITY;
        }

        return total;
    }
}
