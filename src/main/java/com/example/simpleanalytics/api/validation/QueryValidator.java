package com.example.simpleanalytics.api.validation;

import com.example.simpleanalytics.api.*;
import com.example.simpleanalytics.utils.ValidationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class QueryValidator implements Validator<Query> {

    @Override
    public ValidationResult validate(Query input) {
        return Arrays.stream(Validators.values())
                .map(v -> v.validationFunction.apply(input))
                .filter(r -> !r.isValid())
                .findFirst()
                .orElse(ValidationResult.valid());
    }

    private enum Validators {
        VALIDATE_SELECTIONS(input -> {
            if (input.getMetricSelections() == null || input.getMetricSelections().isEmpty()) {
                return new ValidationResult(false, "Missing metricSelection element");
            }
            return ValidationResult.valid();
        }),
        VALIDATE_DATES(input -> {
            if (input.getDimensionsFilters() == null) {
                return ValidationResult.valid();
            }
            List<String> invalidDates = input.getDimensionsFilters().stream()
                    .filter(f -> f.getDimension() == DimensionType.DATE && !ValidationUtils.isValidDate(f.getValue()))
                    .map(DimensionFilter::getValue).collect(Collectors.toList());
            if (!invalidDates.isEmpty()) {
                return new ValidationResult(false, "The following dates are invalid: " + String.join(",", invalidDates));
            }
            return ValidationResult.valid();
        }),
        VALIDATE_GROUP_BY(input -> {
            boolean usingAggregateFunctions = input.getMetricSelections().stream().anyMatch(s -> s.getAggregateFunction() != null);
            if (usingAggregateFunctions) {
                List<String> nonAggregated = input.getMetricSelections().stream().filter(m -> !isAggregated(m)).map(m -> m.getMetricType().getIdentifier()).collect(Collectors.toList());
                List<String> shouldBeInGroupBy;
                if (input.getGroupBy() != null) {
                    shouldBeInGroupBy = nonAggregated.stream().filter(m -> !input.getGroupBy().stream().map(DimensionType::getIdentifier).collect(Collectors.toList()).contains(m)).collect(Collectors.toList());
                } else {
                    shouldBeInGroupBy = nonAggregated;
                }
                if (!shouldBeInGroupBy.isEmpty()) {
                    return new ValidationResult(false, "The following identifiers are needed in the groupBy: " + String.join(",", shouldBeInGroupBy));
                }
            }

            return ValidationResult.valid();
        }),
        VALIDATE_AGGREGATE_FUNCTIONS(input -> {
            List<String> invalidAggregations = input.getMetricSelections().stream()
                    .filter(s -> s.getAggregateFunction() != null && !(s.getMetricType().getType() instanceof MetricType))
                    .map(s -> s.getMetricType().getIdentifier())
                    .collect(Collectors.toList());
            if (!invalidAggregations.isEmpty()) {
                return new ValidationResult(false, "The following identifiers do not support aggregations: " + String.join(",", invalidAggregations));
            }
            return ValidationResult.valid();
        }),
        VALIDATE_DIMENSION_FILTERS(input -> {
            if (input.getDimensionsFilters() == null) {
                return ValidationResult.valid();
            }
            Optional<DimensionFilter> invalidFilter = input.getDimensionsFilters().stream().filter(f -> !(f.getType().getApplicableClass().isAssignableFrom(f.getDimension().getValueClass()))).findAny();
            if (invalidFilter.isPresent()) {
                return new ValidationResult(false, "The filter type " + invalidFilter.get().getType().getIdentifier() + " cannot be used with identifier " + invalidFilter.get().getDimension().getIdentifier());
            }
            return ValidationResult.valid();
        });

        private final Function<Query, ValidationResult> validationFunction;

        Validators(Function<Query, ValidationResult> validationFunction) {
            this.validationFunction = validationFunction;
        }
    }

    private static boolean isAggregated(MetricSelection selection) {
        return selection.getAggregateFunction() != null;
    }
}
