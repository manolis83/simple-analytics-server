package com.example.simpleanalytics;

import com.example.simpleanalytics.api.Query;
import com.example.simpleanalytics.api.validation.QueryValidator;
import com.example.simpleanalytics.api.validation.Validator;
import com.example.simpleanalytics.dto.AnalyticsEntry;
import com.example.simpleanalytics.etl.extractor.CsvFileExtractor;
import com.example.simpleanalytics.etl.extractor.CvsEntryValidator;
import com.example.simpleanalytics.etl.extractor.EntryValidator;
import com.example.simpleanalytics.etl.extractor.Extractor;
import com.example.simpleanalytics.etl.loader.AnalyticsEntryLoader;
import com.example.simpleanalytics.etl.loader.Loader;
import com.example.simpleanalytics.etl.transformer.CSVTransformer;
import com.example.simpleanalytics.etl.transformer.Transformer;
import com.example.simpleanalytics.persistence.AnalyticsRepository;
import com.example.simpleanalytics.persistence.SimpleAnalyticsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("com.example.simpleanalytics.controller")
@EnableJpaRepositories(repositoryBaseClass = SimpleAnalyticsRepository.class)
public class ApplicationConfiguration {
    @Bean
    public Extractor<List<String>> getExtractor(@Value("${input.filepath}") String filepath, EntryValidator<List<String>> validator, ResourceLoader rl) {
        return new CsvFileExtractor(filepath, validator, rl);
    }

    @Bean
    public Transformer<List<List<String>>> getTransformer() {
        return new CSVTransformer();
    }

    @Bean
    public Loader<AnalyticsEntry> getLoader(AnalyticsRepository repository) {
        return new AnalyticsEntryLoader(repository);
    }

    @Bean
    public EntryValidator<List<String>> getEntryValidator() {
        return new CvsEntryValidator();
    }

    @Bean
    public Validator<Query> getQueryValidator() {
        return new QueryValidator();
    }

    @Bean
    public CommandLineRunner getLoadingRunner(
            Extractor<List<String>> extractor, Transformer<List<List<String>>> transformer, Loader<AnalyticsEntry> loader) {
        return new StartupRunner(transformer, extractor, loader);
    }
}
