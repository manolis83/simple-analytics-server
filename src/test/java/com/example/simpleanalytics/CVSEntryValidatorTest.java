package com.example.simpleanalytics;

import com.example.simpleanalytics.etl.extractor.CvsEntryValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CVSEntryValidatorTest {

    @Test
    public void testValid() {
        CvsEntryValidator v = new CvsEntryValidator();

        List<String> entry = new ArrayList<>();
        entry.add("valid datasource");
        entry.add("valid campaign");
        entry.add("11/11/11");
        entry.add("10");
        entry.add("50");

        Assertions.assertThat(v.validate(entry)).isTrue();
    }

    @Test
    public void testInvalidClicks() {
        CvsEntryValidator v = new CvsEntryValidator();

        List<String> entry = new ArrayList<>();
        entry.add("valid datasource");
        entry.add("valid campaign");
        entry.add("11/11/11");
        entry.add("not a number");
        entry.add("50");

        Assertions.assertThat(v.validate(entry)).isFalse();
    }

    @Test
    public void testInvalidDateMonth() {
        CvsEntryValidator v = new CvsEntryValidator();

        List<String> entry = new ArrayList<>();
        entry.add("valid datasource");
        entry.add("valid campaign");
        entry.add("13/11/11");
        entry.add("10");
        entry.add("50");

        Assertions.assertThat(v.validate(entry)).isFalse();
    }

    @Test
    public void testInvalidDateDay() {
        CvsEntryValidator v = new CvsEntryValidator();

        List<String> entry = new ArrayList<>();
        entry.add("valid datasource");
        entry.add("valid campaign");
        entry.add("11/31/11");
        entry.add("10");
        entry.add("50");

        Assertions.assertThat(v.validate(entry)).isFalse();
    }

    @Test
    public void testEmptyImpressions() {
        CvsEntryValidator v = new CvsEntryValidator();

        List<String> entry = new ArrayList<>();
        entry.add("valid datasource");
        entry.add("valid campaign");
        entry.add("11/11/11");
        entry.add("10");
        entry.add("");

        Assertions.assertThat(v.validate(entry)).isFalse();
    }
}
