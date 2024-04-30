package com.example.sick.api.model.request;
public record MailRequest(
        long applicationId,
        String mailSubject,
        String mailText,
        String mailRecipient
) {
}