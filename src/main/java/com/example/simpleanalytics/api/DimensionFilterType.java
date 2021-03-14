package com.example.simpleanalytics.api;

import com.example.simpleanalytics.persistence.AnalyticsEntryEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 * An enum modelling the supported filter types (equal, greater than,..)
 */
public enum DimensionFilterType {
    @JsonProperty("equal")
    EQUAL(Object.class, "equal") {
        @Override
        public Predicate constructPredicate(DimensionType type, Object value, CriteriaBuilder cb, Root<AnalyticsEntryEntity> root) {
            return cb.equal(root.get(type.getIdentifier()), type.bind(value));
        }
    },
    @JsonProperty("lessThan")
    LESS_THAN(Temporal.class, "lessThan") {
        @Override
        public Predicate constructPredicate(DimensionType type, Object value, CriteriaBuilder cb, Root<AnalyticsEntryEntity> root) {
            return cb.lessThan(root.get(type.getIdentifier()).as(LocalDate.class), (LocalDate) type.bind(value));
        }
    },
    @JsonProperty("greaterThan")
    GREATER_THAN(Temporal.class, "greaterThan") {
        @Override
        public Predicate constructPredicate(DimensionType type, Object value, CriteriaBuilder cb, Root<AnalyticsEntryEntity> root) {
            return cb.greaterThan(root.get(type.getIdentifier()).as(LocalDate.class), (LocalDate) type.bind(value));
        }
    };

    private final Class<?> applicableClass;
    private final String identifier;

    DimensionFilterType(Class<?> applicableClass, String identifier) {
        this.applicableClass = applicableClass;
        this.identifier = identifier;
    }

    public Class<?> getApplicableClass() {
        return applicableClass;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract Predicate constructPredicate(DimensionType type, Object value, CriteriaBuilder cb, Root<AnalyticsEntryEntity> root);
}
