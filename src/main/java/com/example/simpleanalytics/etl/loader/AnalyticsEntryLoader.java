package com.example.simpleanalytics.etl.loader;

import com.example.simpleanalytics.dto.AnalyticsEntry;
import com.example.simpleanalytics.persistence.AnalyticsEntryEntity;
import com.example.simpleanalytics.persistence.AnalyticsRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsEntryLoader implements Loader<AnalyticsEntry> {
    private final AnalyticsRepository repository;

    public AnalyticsEntryLoader(AnalyticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void load(List<AnalyticsEntry> records) {
        repository.saveAll(records.stream().map(AnalyticsEntryEntity::new).collect(Collectors.toList()));
    }
}
