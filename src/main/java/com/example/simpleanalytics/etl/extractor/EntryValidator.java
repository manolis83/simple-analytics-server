package com.example.simpleanalytics.etl.extractor;

public interface EntryValidator<O> {
    boolean validate(O entry);
}
