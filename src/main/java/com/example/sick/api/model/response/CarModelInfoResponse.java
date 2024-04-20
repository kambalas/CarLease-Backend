package com.example.sick.api.model.response;

import java.util.List;

public record CarModelInfoResponse(
        List<Variant> variants,
        List<Integer> years,
        List<String> fuelTypes,
        List<Integer> enginePowers,
        List<Double> engineSizes
) {

    public record Variant(
            int id,
            String name
    ) {
    }

}
