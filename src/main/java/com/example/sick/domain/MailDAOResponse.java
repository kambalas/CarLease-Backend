package com.example.sick.domain;


import java.time.LocalDateTime;

public record MailDAOResponse (
        long id,
        long applicationId,
        String mailText,
        LocalDateTime createdAt
){
}
