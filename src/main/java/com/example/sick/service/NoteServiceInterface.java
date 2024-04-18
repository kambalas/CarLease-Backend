package com.example.sick.service;

import com.example.sick.api.model.request.NoteRequest;
import com.example.sick.api.model.response.NoteResponse;

import java.util.List;

public interface NoteServiceInterface {
    List<NoteResponse> getNotesById(long id);
    void createNote(NoteRequest noteRequest);
}
