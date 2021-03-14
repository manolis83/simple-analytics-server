package com.example.simpleanalytics.api.validation;

public interface Validator<T> {
    ValidationResult validate(T input);
}
