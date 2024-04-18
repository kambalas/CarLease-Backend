package com.example.sick.repository;

import com.example.sick.domain.NoteDAORequest;
import com.example.sick.domain.NoteDAOResponse;

import java.util.Optional;

public interface NoteRepositoryInterface {

    Optional<NoteDAOResponse> selectNotesById(long applicationId);

    long createNote(NoteDAORequest note);
}
