package com.example.sick.api.model.response;

import java.time.LocalDateTime;

public record MailTextResponse(
        String mailText,
        LocalDateTime createdAt
) {
}
