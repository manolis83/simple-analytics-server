package com.example.simpleanalytics.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
public class AnalyticsEntry {
    private final String datasource;
    private final String campaign;
    private final LocalDate date;
    private final int clicks;
    private final int impressions;
}
