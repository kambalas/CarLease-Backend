package com.example.sick.api.model.response;

import java.math.BigDecimal;

public record RatesResponse(

        long id,
        BigDecimal carValue,
        int period,
        BigDecimal downPayment,
        long residualValuePercentage,
        Boolean isEcoFriendly,
        BigDecimal monthlyPayment

) {
}
