package com.example.simpleanalytics.etl.extractor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvFileExtractor implements Extractor<List<String>> {
    private final String filename;
    private final EntryValidator<List<String>> validator;
    private final ResourceLoader rl;

    public CsvFileExtractor(String filename, EntryValidator<List<String>> validator, ResourceLoader rl) {
        this.filename = filename;
        this.validator = validator;
        this.rl = rl;
    }

    @Override
    public List<List<String>> extract() {
        try (InputStream in = rl.getResource(filename).getInputStream()) {
            CSVParser p = new CSVParser(new BufferedReader(new InputStreamReader(in)), CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreSurroundingSpaces());

            List<String> sortedKeys = p.getHeaderMap().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).collect(Collectors.toList());

            return p.getRecords().stream().map(r -> getValueList(r, sortedKeys)).filter(validator::validate).collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException("During extraction", e);
        }
    }

    private List<String> getValueList(CSVRecord record, List<String> sortedKeys) {
        final Map<String, String> valueMap = record.toMap();
        return sortedKeys.stream().map(valueMap::get).collect(Collectors.toList());
    }
}
