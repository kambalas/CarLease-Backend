package com.example.sick.domain;

import com.example.sick.utils.ApplicationStatus;

import java.sql.Timestamp;

public record ApplicationListDAOResponse (
    long id,
    String firstName,
    String lastName,
    boolean isOpened,
    ApplicationStatus status,
    Timestamp updatedAt,
    boolean isHighRisk
)
{}