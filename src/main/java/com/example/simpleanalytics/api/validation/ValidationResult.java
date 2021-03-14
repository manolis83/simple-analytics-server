package com.example.simpleanalytics.api.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationResult {
    private static final ValidationResult VALID = new ValidationResult(true, null);

    private final boolean valid;
    private final String error;

    public static ValidationResult valid() {
        return VALID;
    }
}