package com.example.simpleanalytics.api;

/**
 * An interface that contains functionality related to the identity and value class of a metric or dimension.
 */
public interface Identifiable {
    String getIdentifier();

    Class<?> getValueClass();
}
