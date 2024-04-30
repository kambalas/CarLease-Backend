package com.example.sick.api.controller;

import com.example.sick.api.model.response.MailsAndNotesResponse;
import com.example.sick.domain.MailDAOResponse;
import com.example.sick.domain.NoteDAOResponse;
import com.example.sick.service.NoteAndEmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/history/")
public class MailAndNoteListController {
    public NoteAndEmailService mailAndNoteService;

    @Autowired
    public MailAndNoteListController(NoteAndEmailService mailAndNoteService) {
        this.mailAndNoteService = mailAndNoteService;
    }

    @GetMapping("/mails/{id}")
    @Operation(summary = "Get email by application id")
    @Deprecated
    @ResponseStatus(HttpStatus.OK)
    public List<MailDAOResponse>
    getMailById(@PathVariable long id) {
        return mailAndNoteService.getMailById(id);
    }

    @GetMapping("/notes/{id}")
    @Operation(summary = "Get note by application id")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteDAOResponse>
    getNotesById(@PathVariable long id) {
        return mailAndNoteService.getNoteById(id);
    }

    @GetMapping("/note-mail-list/{id}")
    @Operation(summary = "Retrieve a list of notes and mails sorted by " +
            "creation date and time in descending order, filtered by application ID.")
    @ResponseStatus(HttpStatus.OK)
    public MailsAndNotesResponse
    getMailAndNoteById(@PathVariable long id) {
        return mailAndNoteService.getNoteAndMailById(id);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to retrieve notes and mails by application ID: " + e.getMessage());
    }

}
