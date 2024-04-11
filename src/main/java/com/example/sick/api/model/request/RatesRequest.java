package com.example.sick.api.model.request;

import java.math.BigDecimal;

public record RatesRequest(

        BigDecimal carValue,
        int period,
        BigDecimal downPayment,
        double residualValuePercentage,
        Boolean isEcoFriendly

) {
}
