package com.example.simpleanalytics.api;

import com.example.simpleanalytics.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SelectionType {
    @JsonProperty(Constants.CLICKS)
    CLICKS(MetricType.CLICKS),
    @JsonProperty(Constants.IMPRESSIONS)
    IMPRESSIONS(MetricType.IMPRESSIONS),

    @JsonProperty(Constants.DATE)
    TIME(DimensionType.DATE),
    @JsonProperty(Constants.CAMPAIGN)
    CAMPAIGN(DimensionType.CAMPAIGN),
    @JsonProperty(Constants.DATASOURCE)
    DATASOURCE(DimensionType.DATASOURCE);

    private final Identifiable type;

    SelectionType(Identifiable type) {
        this.type = type;
    }

    public Identifiable getType() {
        return type;
    }

    public String getIdentifier() {
        return type.getIdentifier();
    }
}
