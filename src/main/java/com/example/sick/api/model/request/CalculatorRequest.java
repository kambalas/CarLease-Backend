package com.example.sick.api.model.request;

import java.math.BigDecimal;

public record CalculatorRequest(

        BigDecimal carValue,
        int period,
        BigDecimal downPayment,
        int residualValuePercentage,
        Boolean isEcoFriendly

) {
}
