package com.example.simpleanalytics.etl.extractor;

import java.util.List;

/**
 * Extracts data for the warehouse
 * @param <O>
 */
public interface Extractor<O> {
    List<O> extract();
}
