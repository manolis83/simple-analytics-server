package com.example.simpleanalytics.etl.extractor;

import com.example.simpleanalytics.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

public class CvsEntryValidator implements EntryValidator<List<String>> {
    Logger logger = LoggerFactory.getLogger(CvsEntryValidator.class);

    private static final int NUMBER_OF_FIELDS_IN_ENTRY = 5;

    @Override
    public boolean validate(List<String> entry) {

        if (entry.size() != NUMBER_OF_FIELDS_IN_ENTRY) {
            logger.warn("Record " + entry + " does not contain the expected number of fields. Skipping.");
            return false;
        }
        if(entry.stream().anyMatch(String::isEmpty)) {
            logger.warn("Encountered empty field while extracting record " + entry + ". Skipping");
            return false;
        }

        if (!ValidationUtils.isValidDate(entry.get(2))) {
            logger.warn("While extracting record " + entry + ":" + entry.get(2) + " is not a valid date string. Skipping");
            return false;
        }

        Optional<String> invalidInteger = IntStream.range(3, 5).mapToObj(entry::get).filter(s -> !isValidInteger(s)).findAny();
        if (invalidInteger.isPresent()) {
            logger.warn("While extracting record " + entry + ": Malformed number " + invalidInteger.get());
            return false;
        }

        return true;
    }

    private boolean isValidInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
