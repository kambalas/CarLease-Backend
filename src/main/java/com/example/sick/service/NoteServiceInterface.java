package com.example.sick.service;

import com.example.sick.api.model.request.NoteRequest;
import com.example.sick.api.model.response.NoteResponse;

public interface NoteServiceInterface {
    NoteResponse getNotesById(long id);
    void createNote(NoteRequest noteRequest);
}
