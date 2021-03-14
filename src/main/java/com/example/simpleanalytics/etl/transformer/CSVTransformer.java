package com.example.simpleanalytics.etl.transformer;

import com.example.simpleanalytics.dto.AnalyticsEntry;
import com.example.simpleanalytics.utils.Constants;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CSVTransformer implements Transformer<List<List<String>>> {

    @Override
    public List<AnalyticsEntry> transform(List<List<String>> input) {
        return input.stream().map(CSVTransformer::transformRecord).collect(Collectors.toList());
    }

    private static AnalyticsEntry transformRecord(List<String> record) {
        return new AnalyticsEntry(
                record.get(0),
                record.get(1),
                LocalDate.parse(record.get(2), Constants.DATE_FORMATTER),
                Integer.parseInt(record.get(3)),
                Integer.parseInt(record.get(4))
        );
    }

}
