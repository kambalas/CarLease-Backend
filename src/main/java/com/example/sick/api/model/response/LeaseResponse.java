package com.example.sick.api.model.response;

public record LeaseResponse(
        long id,
        String make,
        String model,
        String modelVariant,
        String year,
        String fuelType,
        Double enginePower,
        Double engineSize,
        String url,
        String offer,
        Boolean terms,
        Boolean confirmation
) {
}
