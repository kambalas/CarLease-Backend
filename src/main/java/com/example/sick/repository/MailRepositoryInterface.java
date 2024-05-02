package com.example.sick.repository;

import com.example.sick.domain.MailDAORequest;
import com.example.sick.domain.MailDAOResponse;
import com.example.sick.domain.NoteDAORequest;
import com.example.sick.domain.NoteDAOResponse;

import java.util.List;
import java.util.Optional;

public interface MailRepositoryInterface {

    List<MailDAOResponse> selectMailByApplicationId(long applicationId);

    void createMail(MailDAORequest note);
}
