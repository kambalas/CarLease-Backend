package com.example.sick.api.model.response;

public record ApplicationMonthlyCountResponse(
        int thisMonthCount,
        int previousMonthCount
) {
}
