package com.example.b07group57.utils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get today's date in UTC.
     */
    public static String getToday() {
        return LocalDate.now(ZoneId.of("UTC")).format(DATE_FORMATTER);
    }

    /**
     * Subtract days in UTC.
     */
    public static String subtractDays(int days) {
        return LocalDate.now(ZoneId.of("UTC")).minusDays(days).format(DATE_FORMATTER);
    }

    /**
     * Subtract months in UTC.
     */
    public static String subtractMonths(int months) {
        return LocalDate.now(ZoneId.of("UTC")).minusMonths(months).format(DATE_FORMATTER);
    }

    /**
     * Subtract years in UTC.
     */
    public static String subtractYears(int years) {
        return LocalDate.now(ZoneId.of("UTC")).minusYears(years).format(DATE_FORMATTER);
    }

    // Method to calculate the difference between two dates
    public static long getDateDifference(String date1, String date2) {
        try {
            // Parse the input date strings
            Date parsedDate1 = DATE_FORMAT.parse(date1);
            Date parsedDate2 = DATE_FORMAT.parse(date2);

            // Calculate the difference in milliseconds
            long differenceInMillis = Math.abs(parsedDate1.getTime() - parsedDate2.getTime());

            // Convert milliseconds to days
            return differenceInMillis / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
