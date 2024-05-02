package com.example.sick.service;

import com.example.sick.api.model.exception.StatusNotFoundException;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.StatusResponse;

public interface StatusService {
    void updateStatusById(StatusRequest statusRequest);
    StatusResponse getStatusById(long id) throws StatusNotFoundException;
    void updateStatusIsRead(long id);
}
