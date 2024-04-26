package com.example.sick.service;

import com.example.sick.api.model.response.MailTextResponse;
import com.example.sick.api.model.response.MailsAndNotesResponse;
import com.example.sick.api.model.response.NotesTextResponse;
import com.example.sick.domain.MailDAOResponse;
import com.example.sick.domain.NoteDAOResponse;
import com.example.sick.repository.MailRepository;
import com.example.sick.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteAndEmailService {
    private final NoteRepository noteRepo;
    private final MailRepository mailRepo;


    public NoteAndEmailService(NoteRepository noteRepo, MailRepository mailRepo) {
        this.noteRepo = noteRepo;
        this.mailRepo = mailRepo;


    }

    public List<NoteDAOResponse> getNoteById(long id) {
        return noteRepo.selectNotesById(id);
    }

    public List<MailDAOResponse> getMailById(long id) {
        return mailRepo.selectMailByApplicationId(id);
    }

    public MailsAndNotesResponse getNoteAndMailById(long id) {
        List<MailDAOResponse> responseMails = mailRepo.selectMailByApplicationId(id);
        List<NoteDAOResponse> responseNotes = noteRepo.selectNotesById(id);

        List<NotesTextResponse> combinedNotesResponses = new ArrayList<>();
        List<MailTextResponse> combinedMailsResponses = new ArrayList<>();

        for (NoteDAOResponse note : responseNotes) {
            combinedNotesResponses.add(convertDAOResponseIntoNoteResponseList(note));
        }
        for (MailDAOResponse mail : responseMails) {
            combinedMailsResponses.add(convertDAOResponseIntoMailResponseList(mail));
        }

        return new MailsAndNotesResponse(id, combinedNotesResponses, combinedMailsResponses);
    }

    private NotesTextResponse convertDAOResponseIntoNoteResponseList(NoteDAOResponse noteDAOResponse) {
        return new NotesTextResponse(
                noteDAOResponse.noteText(),
                noteDAOResponse.createdAt()
        );
    }

    private MailTextResponse convertDAOResponseIntoMailResponseList(MailDAOResponse mailDAOResponse) {
        return new MailTextResponse(
                mailDAOResponse.mailText(),
                mailDAOResponse.createdAt()
        );
    }
}
