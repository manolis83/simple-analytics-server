package com.example.simpleanalytics.persistence;

import com.example.simpleanalytics.api.MetricSelection;
import com.example.simpleanalytics.api.Query;
import com.example.simpleanalytics.api.SelectionType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleAnalyticsRepository extends SimpleJpaRepository<AnalyticsEntryEntity, Long> implements AnalyticsRepository {

    private final EntityManager em;

    public SimpleAnalyticsRepository(Class<AnalyticsEntryEntity> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    public SimpleAnalyticsRepository(JpaEntityInformation<AnalyticsEntryEntity, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }

    @Override
    public List<Map<String, Object>> retrieve(Query query) {
        List<Tuple> tuples = query.execute(em);

        return tuples.stream()
                .map(t -> getResultMap(t,
                        query.getMetricSelections().stream().map(MetricSelection::getMetricType).collect(Collectors.toList()))
                )
                .collect(Collectors.toList());
    }

    private Map<String, Object> getResultMap(Tuple values, List<SelectionType> types) {
        Map<String, Object> result = new LinkedHashMap<>();
        IntStream.range(0, types.size()).forEach(i -> result.put(types.get(i).getIdentifier(), values.get(i)));
        return result;
    }
}
