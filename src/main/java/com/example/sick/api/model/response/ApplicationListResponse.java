package com.example.sick.api.model.response;

import com.example.sick.utils.ApplicationStatus;

import java.sql.Timestamp;

public record ApplicationListResponse (
    long id,
    String firstName,
    String lastName,
    boolean isOpened,
    ApplicationStatus status,
    Timestamp updatedAt,
    boolean isHighRisk
) {}
