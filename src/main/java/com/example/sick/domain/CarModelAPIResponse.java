package com.example.sick.domain;

import java.util.List;
import java.util.Map;

public record CarModelAPIResponse(
        Map<String, Object> collection,
        List<APIResponseModelData> data
) {
    public record APIResponseModelData(
            int id,
            int make_id,
            String name
    ) {
    }
}
