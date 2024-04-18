package com.example.sick.domain;

public record MailDAORequest(
        long applicationId,
        String mailText
) {
}
