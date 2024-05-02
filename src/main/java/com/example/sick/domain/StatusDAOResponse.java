package com.example.sick.domain;

import com.example.sick.utils.ApplicationStatus;

import java.time.LocalDateTime;

public record StatusDAOResponse(

        long id,
        ApplicationStatus APPLICATIONSTATUS,
        Boolean isOpened,
        LocalDateTime updatedAt,
        LocalDateTime createdAt,
        Boolean isHighRisk

) {
}
