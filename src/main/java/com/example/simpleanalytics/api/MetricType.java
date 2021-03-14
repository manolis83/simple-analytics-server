package com.example.simpleanalytics.api;

import com.example.simpleanalytics.utils.Constants;

public enum MetricType implements Identifiable {
    CLICKS(Constants.CLICKS, Long.class),
    IMPRESSIONS(Constants.IMPRESSIONS, Long.class);

    private final String identifier;
    private final Class<?> valueClass;

    MetricType(String identifier, Class<?> valueClass) {

        this.identifier = identifier;
        this.valueClass = valueClass;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }
}
