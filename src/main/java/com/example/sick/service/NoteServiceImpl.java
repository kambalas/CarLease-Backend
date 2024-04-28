package com.example.sick.service;

import com.example.sick.api.model.request.NoteRequest;
import com.example.sick.api.model.response.NoteResponse;
import com.example.sick.domain.NoteDAORequest;
import com.example.sick.domain.NoteDAOResponse;
import com.example.sick.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteServiceInterface {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<NoteResponse> getNotesById(long id) {

        return noteRepository.selectNotesById(id).stream()
                .map(this::convertDaoResponseIntoNoteResponse)
                .toList();

    }

    @Override
    public void createNote(NoteRequest noteRequest) {

        if (null == noteRequest|| null == noteRequest.noteText() || noteRequest.noteText().isEmpty()) {
            throw new IllegalArgumentException("Note request must not be null");
        }
        String noteText = noteRequest.noteText() == null ? "" : noteRequest.noteText();
        NoteDAORequest daoRequest = new NoteDAORequest(noteRequest.applicationId(), noteText);

        noteRepository.createNote(daoRequest);
    }

    private NoteResponse convertDaoResponseIntoNoteResponse(NoteDAOResponse note) {
        return note == null
                ? null
                : new NoteResponse(note.noteText());
    }
}
