package com.example.sick.domain;

import com.example.sick.utils.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationListDAOResponse (
    long id,
    String firstName,
    String lastName,
    boolean isOpened,
    ApplicationStatus status,
    LocalDateTime updatedAt,
    boolean isHighRisk
)
{}