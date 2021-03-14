package com.example.simpleanalytics.etl.transformer;

import com.example.simpleanalytics.dto.AnalyticsEntry;

import java.util.List;

public interface Transformer<I> {
    List<AnalyticsEntry> transform(I input);
}
