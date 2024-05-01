package com.example.sick.domain;

import java.time.LocalDateTime;

public record ApplicationDailyCountDAOResponse(
        LocalDateTime day,
        int applicationCount
) {
}
