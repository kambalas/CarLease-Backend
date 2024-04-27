package com.example.sick;

import com.example.sick.domain.NoteDAOResponse;
import com.example.sick.repository.MailRepository;
import com.example.sick.repository.NoteRepository;
import com.example.sick.service.NoteAndEmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteAndEmailServiceTest {
    @Mock
    private NoteRepository noteRepo;

    @Mock
    private MailRepository mailRepo;

    private NoteAndEmailService service;

    @Before
    public void setUp() {
        service = new NoteAndEmailService(noteRepo, mailRepo);
    }
    @Test
    public void testGetNoteById_ReturnsEmptyList_WhenNoNoteFound() {
        when(noteRepo.selectNotesById(1L)).thenReturn(Collections.emptyList());
        List<NoteDAOResponse> responseNotes = service.getNoteById(1L);
        assertEquals(Collections.emptyList(), responseNotes);
    }

}
