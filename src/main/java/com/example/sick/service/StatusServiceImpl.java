package com.example.sick.service;

import com.example.sick.api.model.exception.StatusNotFoundException;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.domain.StatusDAORequest;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.repository.StatusRepository;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void updateStatusRead(long id) {
        statusRepository.updateStatusRead(id);
    }

    @Override
    public void updateStatusById(StatusRequest statusRequest) {
        statusRepository.updateStatusById(convertToStatusDAORequest(statusRequest));
    }

    @Override
    public StatusResponse getStatusById(long id) throws StatusNotFoundException {
        StatusDAOResponse statusDAOResponse = statusRepository.getStatusById(id).orElseThrow(() -> new StatusNotFoundException(id));
        return convertStatusDAOResponseToStatusResponse(statusDAOResponse);
    }

    private StatusDAORequest convertToStatusDAORequest(StatusRequest statusRequest) {
        return new StatusDAORequest(statusRequest.id(), ApplicationStatus.valueOf(statusRequest.APPLICATIONSTATUS()));
    }

    private StatusResponse convertStatusDAOResponseToStatusResponse(StatusDAOResponse statusDAOResponse) {
        return new StatusResponse(
                statusDAOResponse.id(),
                statusDAOResponse.APPLICATIONSTATUS(),
                statusDAOResponse.isOpened(),
                statusDAOResponse.updatedAt(),
                statusDAOResponse.createdAt()
        );
    }
}
