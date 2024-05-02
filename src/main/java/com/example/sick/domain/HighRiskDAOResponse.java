package com.example.sick.domain;

public record HighRiskDAOResponse(
        int currentMonthCount,
        int lastMonthCount
) {
}
