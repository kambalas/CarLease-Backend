package com.example.sick.repository;

import com.example.sick.domain.StatusDAORequest;
import com.example.sick.domain.StatusDAOResponse;

import java.util.List;
import java.util.Optional;

public interface StatusRepositoryInterface {
    void updateStatusRead(long id, boolean isOpened);

    void createStatus(long id, boolean isHighRisk);

    void updateStatusById(StatusDAORequest status);

     Optional<StatusDAOResponse> getStatusById(long id);

     List<StatusDAOResponse> getAllStatus();

    List<StatusDAOResponse> getAllStatusByPage(long pageNumber);
}
