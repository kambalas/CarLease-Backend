package com.example.sick.domain;

import java.math.BigDecimal;

public record AcceptedApplicationDAOResponse(
        BigDecimal thisYearSum,
        BigDecimal lastYearSum
) {
}
