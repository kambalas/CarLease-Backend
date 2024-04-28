package com.example.sick.api.model.response;

import com.example.sick.utils.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationListResponse (
    long id,
    String firstName,
    String lastName,
    boolean isOpened,
    ApplicationStatus status,
    LocalDateTime updatedAt,
    boolean isHighRisk
) {}
