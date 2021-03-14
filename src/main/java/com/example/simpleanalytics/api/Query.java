package com.example.simpleanalytics.api;

import com.example.simpleanalytics.persistence.AnalyticsEntryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Query {
    private List<MetricSelection> metricSelections;
    private Set<DimensionFilter> dimensionsFilters;
    private Set<DimensionType> groupBy;

    public List<Tuple> execute(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<AnalyticsEntryEntity> root = cq.from(AnalyticsEntryEntity.class);

        // select
        if (getMetricSelections() != null) {
            List<Selection<?>> selections = getMetricSelections().stream().map(m -> m.createSelection(root, cb)).collect(Collectors.toList());

            cq.multiselect(selections);
        }
        // where
        if (getDimensionsFilters() != null) {
            List<Predicate> where = getDimensionsFilters().stream().map(f -> f.getType().constructPredicate(f.getDimension(), f.getValue(), cb, root)).collect(Collectors.toList());

            cq.where(cb.and(where.toArray(new Predicate[]{})));
        }
        // group by
        if (getGroupBy() != null) {
            cq.groupBy(getGroupBy().stream().map(e -> root.get(e.getIdentifier())).collect(Collectors.toList()));
        }

        return em.createQuery(cq).getResultList();
    }
}
