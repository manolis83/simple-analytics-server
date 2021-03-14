package com.example.simpleanalytics;

import com.example.simpleanalytics.api.*;
import com.example.simpleanalytics.api.validation.QueryValidator;
import com.example.simpleanalytics.api.validation.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryValidatorTest {

    @Test
    public void testValidAggregation() {
        // Given
        Query q = new Query();
        MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, null);
        MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, AggregateFunction.SUM);
        List<MetricSelection> selections = new ArrayList<>();
        selections.add(campaignSelection);
        selections.add(impressionSelection);
        q.setMetricSelections(selections);
        q.setGroupBy(Collections.singleton(DimensionType.CAMPAIGN));

        // When
        QueryValidator v = new QueryValidator();
        ValidationResult validationResult = v.validate(q);

        // Then
        Assertions.assertThat(validationResult.isValid()).isTrue();
        Assertions.assertThat(validationResult.getError()).isNull();
    }

    @Test
    public void testEmptySelections() {
        // Given
        Query q = new Query();
        QueryValidator v = new QueryValidator();
        // When
        ValidationResult validationResult = v.validate(q);

        // Then
        Assertions.assertThat(validationResult.isValid()).isFalse();
        Assertions.assertThat(validationResult.getError()).isEqualTo("Missing metricSelection element");
    }

    @Test
    public void testInvalidGroupBy() {
        // Given
        Query q = new Query();
        MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, null);
        MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, AggregateFunction.SUM);
        List<MetricSelection> selections = new ArrayList<>();
        selections.add(campaignSelection);
        selections.add(impressionSelection);
        q.setMetricSelections(selections);
        QueryValidator v = new QueryValidator();

        // When
        ValidationResult validationResult = v.validate(q);

        // Then
        Assertions.assertThat(validationResult.isValid()).isFalse();
        Assertions.assertThat(validationResult.getError()).isEqualTo("The following identifiers are needed in the groupBy: campaign");
    }

    @Test
    public void testInvalidAggregation() {
        // Given
        Query q = new Query();
        MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, AggregateFunction.SUM);
        MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, AggregateFunction.SUM);
        List<MetricSelection> selections = new ArrayList<>();
        selections.add(campaignSelection);
        selections.add(impressionSelection);
        q.setMetricSelections(selections);
        QueryValidator v = new QueryValidator();

        // When
        ValidationResult validationResult = v.validate(q);

        // Then
        Assertions.assertThat(validationResult.isValid()).isFalse();
        Assertions.assertThat(validationResult.getError()).isEqualTo("The following identifiers do not support aggregations: campaign");
    }
}
