package com.example.simpleanalytics.api;

import com.example.simpleanalytics.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * An enum modelling the supported dimensions
 */
public enum DimensionType implements Identifiable {
    @JsonProperty(Constants.DATE)
    DATE(LocalDate.class, Constants.DATE) {
        @Override
        public Object bind(Object value) {
            if (value instanceof String) {
                return LocalDate.parse((String) value, Constants.DATE_FORMATTER);
            }
            return value;
        }
    },
    @JsonProperty(Constants.CAMPAIGN)
    CAMPAIGN(String.class, Constants.CAMPAIGN),
    @JsonProperty(Constants.DATASOURCE)
    DATASOURCE(String.class, Constants.DATASOURCE);

    private final Class<?> valueClass;
    private final String identifier;

    DimensionType(Class<?> valueClass, String identifier) {
        this.valueClass = valueClass;
        this.identifier = identifier;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public Object bind(Object value) {
        return value;
    }
}
