package com.example.sick.domain;

public record StatusCountDAOResponse(
    int newCount,
    int acceptedCount,
    int rejectedCount,
    int pendingCount
){
}
