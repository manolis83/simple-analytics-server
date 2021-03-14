package com.example.simpleanalytics.api;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    private final List<Map<String, Object>> data;
    private final String error;

    public static Response withData(List<Map<String, Object>> data) {
        return new Response(data, null);
    }

    public static Response withError(String error) {
        return new Response(null, error);
    }
}
