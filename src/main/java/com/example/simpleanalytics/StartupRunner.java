package com.example.simpleanalytics;

import com.example.simpleanalytics.dto.AnalyticsEntry;
import com.example.simpleanalytics.etl.extractor.Extractor;
import com.example.simpleanalytics.etl.loader.Loader;
import com.example.simpleanalytics.etl.transformer.Transformer;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

public class StartupRunner implements CommandLineRunner {
    private final Transformer<List<List<String>>> transformer;
    private final Extractor<List<String>> extractor;
    private final Loader<AnalyticsEntry> loader;

    public StartupRunner(Transformer<List<List<String>>> transformer, Extractor<List<String>> extractor, Loader<AnalyticsEntry> loader) {
        this.transformer = transformer;
        this.extractor = extractor;
        this.loader = loader;
    }

    @Override
    public void run(String... args) throws Exception {
        List<AnalyticsEntry> entries = transformer.transform(extractor.extract());
        loader.load(entries);
    }
}
