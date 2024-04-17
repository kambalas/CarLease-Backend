package com.example.sick.api.model.request;

public record NoteRequest(

        long applicationId,
        String noteText
) {
}
