package com.example.simpleanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * A dimension filter used to select specific entries e.g. "date greater than 11/12/19"
 */
public class DimensionFilter {
    private DimensionFilterType type;
    private DimensionType dimension;
    private String value;
}
