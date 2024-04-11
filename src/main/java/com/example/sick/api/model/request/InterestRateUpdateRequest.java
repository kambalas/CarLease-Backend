package com.example.sick.api.model.request;

import java.math.BigDecimal;

public record InterestRateUpdateRequest(
        String carType,
        BigDecimal rate
) {
}
