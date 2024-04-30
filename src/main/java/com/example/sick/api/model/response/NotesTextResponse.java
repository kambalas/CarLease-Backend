package com.example.sick.api.model.response;

import java.time.LocalDateTime;

public record NotesTextResponse(
        String notesText,
        LocalDateTime createdAt
) {
}
