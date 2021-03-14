package com.example.simpleanalytics.persistence;

import com.example.simpleanalytics.api.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Map;

public interface AnalyticsRepository extends JpaRepositoryImplementation<AnalyticsEntryEntity, Long> {
    List<Map<String, Object>> retrieve(Query query);
}
