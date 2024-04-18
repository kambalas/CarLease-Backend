package com.example.sick.api.model.request;
public record MailRequest(
        long applicationId,
        String mailText,
        String mailRecipient
) {
}