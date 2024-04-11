package com.example.sick.api.model.response;

import java.math.BigInteger;

public record LeaseResponse(
        BigInteger id,
        String make,
        String mode,
        String modelVariant,
        String year,
        String fuelType,
        Double enginePower,
        String url,
        String offer,
        Boolean terms,
        Boolean confirmation
) {
}
