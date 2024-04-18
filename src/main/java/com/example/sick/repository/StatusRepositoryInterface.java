package com.example.sick.repository;

import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.domain.StatusDAORequest;
import com.example.sick.domain.StatusDAOResponse;

import java.util.List;
import java.util.Optional;

public interface StatusRepositoryInterface {
    void createStatus(long id);

    void updateStatusById(StatusDAORequest status);

     Optional<StatusDAOResponse> getStatusById(long id);

     List<StatusDAOResponse> getAllStatus();

    List<StatusDAOResponse> getAllStatusByPage(long pageNumber);
}
