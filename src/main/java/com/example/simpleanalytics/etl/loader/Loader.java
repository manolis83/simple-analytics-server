package com.example.simpleanalytics.etl.loader;

import java.util.List;

public interface Loader<O> {
    void load(List<O> records);
}
