package com.example.sick.api.model.response;


import com.example.sick.utils.ApplicationStatus;

import java.time.LocalDateTime;


public record StatusResponse(
        long id,

        ApplicationStatus APPLICATIONSTATUS,

        Boolean isOpened,

        LocalDateTime updatedAt,

        LocalDateTime createdAt,

        Boolean isHighRisk
) {
}
