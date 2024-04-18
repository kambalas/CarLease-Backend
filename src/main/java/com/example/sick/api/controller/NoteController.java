package com.example.sick.api.controller;

import com.example.sick.api.model.request.NoteRequest;
import com.example.sick.api.model.response.NoteResponse;
import com.example.sick.service.NoteServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notes")
@CrossOrigin("*")
public class NoteController {

    NoteServiceImpl noteService;

    @Autowired
    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNote(@RequestBody NoteRequest noteRequest) {
        noteService.createNote(noteRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notes by application id")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse getNotesById(@PathVariable long id) {
        return noteService.getNotesById(id);
    }
}
