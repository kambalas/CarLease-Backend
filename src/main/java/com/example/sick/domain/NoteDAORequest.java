package com.example.sick.domain;

public record NoteDAORequest(
        long applicationId,
        String noteText
) {
}
