package com.example.sick.api.model.response;

public record HighRiskResponse(
        int currentMonthCount,
        int lastMonthCount
) {
}
