package com.example.sick.repository;

import com.example.sick.domain.NoteDAORequest;
import com.example.sick.domain.NoteDAOResponse;

import java.util.List;
import java.util.Optional;

public interface NoteRepositoryInterface {

    List<NoteDAOResponse> selectNotesById(long applicationId);

    long createNote(NoteDAORequest note);
}
