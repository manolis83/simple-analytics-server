package com.example.simpleanalytics;

import com.example.simpleanalytics.api.*;
import com.example.simpleanalytics.controller.AnalyticsController;
import com.example.simpleanalytics.utils.Constants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class AnalyticsControllerIntegrationTest {
	@Autowired
	private AnalyticsController controller;

	@Test
	void testSum() {
		// Given
		Query q = new Query();
		MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, null);
		MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, AggregateFunction.SUM);
		List<MetricSelection> selections = new ArrayList<>();
		selections.add(campaignSelection);
		selections.add(impressionSelection);
		q.setMetricSelections(selections);

		q.setDimensionsFilters(Collections.singleton(new DimensionFilter(DimensionFilterType.EQUAL,
				DimensionType.CAMPAIGN, "Campaign 1")));
		q.setGroupBy(Collections.singleton(DimensionType.CAMPAIGN));

		// When
		Response response = controller.retrieveAnalytics(q);

		// Then
		Assertions.assertThat(response.getError()).isNull();
		Assertions.assertThat(response.getData()).isNotNull();
		Assertions.assertThat(response.getData().size()).isEqualTo(1);
		Assertions.assertThat(response.getData().get(0).get(Constants.IMPRESSIONS)).isEqualTo(526L);
	}

	@Test
	void testSumNonExistentCampaign() {
		// Given
		Query q = new Query();
		MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, null);
		MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, AggregateFunction.SUM);
		List<MetricSelection> selections = new ArrayList<>();
		selections.add(campaignSelection);
		selections.add(impressionSelection);
		q.setMetricSelections(selections);

		q.setDimensionsFilters(Collections.singleton(new DimensionFilter(DimensionFilterType.EQUAL,
				DimensionType.CAMPAIGN, "Non-existent campaign")));
		q.setGroupBy(Collections.singleton(DimensionType.CAMPAIGN));

		// When
		Response response = controller.retrieveAnalytics(q);

		// Then
		Assertions.assertThat(response.getError()).isNull();
		Assertions.assertThat(response.getData()).isNotNull();
		Assertions.assertThat(response.getData().size()).isEqualTo(0);
	}

	@Test
	void testNonExistentDate() {
		// Given
		Query q = new Query();
		MetricSelection campaignSelection = new MetricSelection(SelectionType.CAMPAIGN, null);
		MetricSelection datasourceSelection = new MetricSelection(SelectionType.DATASOURCE, null);
		MetricSelection impressionSelection = new MetricSelection(SelectionType.IMPRESSIONS, null);

		List<MetricSelection> selections = new ArrayList<>();
		selections.add(campaignSelection);
		selections.add(datasourceSelection);
		selections.add(impressionSelection);
		q.setMetricSelections(selections);

		q.setDimensionsFilters(Collections.singleton(new DimensionFilter(DimensionFilterType.EQUAL,
				DimensionType.DATE, "11/11/20")));

		// When
		Response response = controller.retrieveAnalytics(q);

		// Then
		Assertions.assertThat(response.getError()).isNull();
		Assertions.assertThat(response.getData()).isNotNull();
		Assertions.assertThat(response.getData().size()).isEqualTo(0);
	}

	@Test
	void testGreaterThanDate() {
		// Given
		Query q = new Query();
		MetricSelection impressionSelection = new MetricSelection(SelectionType.CLICKS, AggregateFunction.SUM);

		List<MetricSelection> selections = new ArrayList<>();
		selections.add(impressionSelection);
		q.setMetricSelections(selections);

		q.setDimensionsFilters(Collections.singleton(new DimensionFilter(DimensionFilterType.GREATER_THAN,
				DimensionType.DATE, "11/16/19")));

		// When
		Response response = controller.retrieveAnalytics(q);

		// Then
		Assertions.assertThat(response.getError()).isNull();
		Assertions.assertThat(response.getData()).isNotNull();
		Assertions.assertThat(response.getData().size()).isEqualTo(1);
		Assertions.assertThat(response.getData().get(0).get(Constants.CLICKS)).isEqualTo(26L);

	}

}
