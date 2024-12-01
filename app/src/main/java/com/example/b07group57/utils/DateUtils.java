package com.example.b07group57.utils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
}
