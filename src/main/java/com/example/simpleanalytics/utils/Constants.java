package com.example.simpleanalytics.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/uu").withResolverStyle(ResolverStyle.STRICT);

    public static final String DATASOURCE = "datasource";
    public static final String CAMPAIGN = "campaign";
    public static final String DATE = "date";
    public static final String CLICKS = "clicks";
    public static final String IMPRESSIONS = "impressions";
}
