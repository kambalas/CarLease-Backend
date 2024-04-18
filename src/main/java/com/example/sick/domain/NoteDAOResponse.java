package com.example.sick.domain;

public record NoteDAOResponse(
        long id,
        long applicationId,
        String noteText
) {
}
