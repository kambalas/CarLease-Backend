package com.example.sick.api.model.response;

import java.util.List;

public record CarVariantInfoResponse(
        List<Integer> years,
        List<String> fuelTypes,
        List<Integer> enginePowers,
        List<String> engineSizes
) {
}

