package com.example.sick.api.model.response;

import java.math.BigDecimal;

public record AcceptedApplicationResponse(
        BigDecimal thisYearSum,
        BigDecimal lastYearSum
) {
}
