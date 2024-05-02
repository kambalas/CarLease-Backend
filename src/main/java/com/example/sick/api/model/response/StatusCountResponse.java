package com.example.sick.api.model.response;

public record StatusCountResponse(
        int newCount,
        int acceptedCount,
        int rejectedCount,
        int pendingCount
) {

}
