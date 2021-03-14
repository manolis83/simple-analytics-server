package com.example.simpleanalytics.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtils {
    public static boolean isValidDate(String date) {
        try {
            Constants.DATE_FORMATTER.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
