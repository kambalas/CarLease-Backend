package com.example.sick.api.model.response;


import com.example.sick.utils.ApplicationStatus;

import java.sql.Timestamp;


public record StatusResponse(
        long id,

        ApplicationStatus APPLICATIONSTATUS,

        Boolean isOpened,

        Timestamp updatedAt,

        Timestamp createdAt,

        Boolean isHighRisk
) {
}
