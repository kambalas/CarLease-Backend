package com.example.sick.api.model.response;

import java.util.List;

public record MailsAndNotesResponse
        (
                long applicationId,
                List<NotesTextResponse> notesTexts,
                List<MailTextResponse> mailTexts
        ) {
}