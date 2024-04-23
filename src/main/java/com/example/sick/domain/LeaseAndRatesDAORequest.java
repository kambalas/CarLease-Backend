package com.example.sick.domain;

import java.math.BigDecimal;

public record LeaseAndRatesDAORequest(

        String make,
        String model,
        String modelVariant,
        String year,
        String fuelType,
        Double enginePower,
        Double engineSize,
        String url,
        byte[] offer,
        Boolean terms,
        Boolean confirmation,
        BigDecimal carValue,
        int period,
        BigDecimal downPayment,
        int residualValuePercentage,
        Boolean isEcoFriendly,
        BigDecimal monthlyPayment

) {
}
