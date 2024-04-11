package com.example.sick.api.model.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public record RatesResponse(

        BigInteger id,
        BigDecimal carValue,
        int period,
        BigDecimal downPayment,
        double residualValuePercentage,
        Boolean isEcoFriendly

) {
}
