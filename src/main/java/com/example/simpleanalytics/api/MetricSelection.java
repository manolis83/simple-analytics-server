package com.example.simpleanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * A metric intended to be selected or aggregated on.
 */
public class MetricSelection {
    private SelectionType metricType;
    private AggregateFunction aggregateFunction;

    public Selection<?> createSelection(Root<?> root, CriteriaBuilder cb) {
        if (aggregateFunction == null) {
            return root.get(metricType.getIdentifier());
        } else {
            return aggregateFunction.createSelection(root.get(metricType.getIdentifier()), cb);
        }
    }
}
