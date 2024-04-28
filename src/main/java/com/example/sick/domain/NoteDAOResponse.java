package com.example.sick.domain;

import io.opencensus.common.Timestamp;

import java.time.LocalDateTime;

public record NoteDAOResponse(
        long id,
        long applicationId,
        String noteText,
        LocalDateTime createdAt
) {
}
