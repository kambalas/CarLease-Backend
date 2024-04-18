package com.example.sick.service;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.GeneralFormsResponse;

import java.util.List;

public interface GeneralFormService {
    List<GeneralFormsResponse> selectAllApplicationsByPage(long pageNumber);
    GeneralFormsResponse getApplicationById(long id)  throws ApplicationNotFoundException;
    void createApplication(GeneralFormsRequest generalFormsRequest);
}
