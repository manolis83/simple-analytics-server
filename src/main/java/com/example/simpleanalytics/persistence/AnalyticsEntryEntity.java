package com.example.simpleanalytics.persistence;

import com.example.simpleanalytics.dto.AnalyticsEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnalyticsEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String datasource;
    private String campaign;
    private LocalDate date;
    private int clicks;
    private int impressions;

    public AnalyticsEntryEntity(AnalyticsEntry entry) {
        this.datasource = entry.getDatasource();
        this.campaign = entry.getCampaign();
        this.date = entry.getDate();
        this.clicks = entry.getClicks();
        this.impressions = entry.getImpressions();
    }
}
