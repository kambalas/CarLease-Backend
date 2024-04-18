package com.example.sick.domain;

import java.util.List;
import java.util.Map;

public record CarMakeAPIResponse(
        Map<String, Object> collection,
        List<Map<String, Object>> data
) {
}
