package com.example.sick.api.model.request;

public record LeaseRequest(

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