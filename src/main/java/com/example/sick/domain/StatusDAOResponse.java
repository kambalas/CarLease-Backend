package com.example.sick.domain;

import com.example.sick.utils.ApplicationStatus;

import java.sql.Timestamp;

public record StatusDAOResponse(

        long id,
        ApplicationStatus APPLICATIONSTATUS,
        Boolean isOpened,
        Timestamp updatedAt,
        Timestamp createdAt

) {
}
