package com.example.simpleanalytics.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

/**
 * An enum modelling the aggregate functions supported
 */
public enum AggregateFunction {
    @JsonProperty("sum")
    SUM {
        @Override
        public Selection<?> createSelection(Path<? extends Number> path, CriteriaBuilder cb) {
            return cb.sum(path);
        }
    },
    @JsonProperty("avg")
    AVG {
        @Override
        public Selection<?> createSelection(Path<? extends Number> path, CriteriaBuilder cb) {
            return cb.avg(path);
        }
    };

    public abstract Selection<?> createSelection(Path<? extends Number> path, CriteriaBuilder cb);
}
