package com.example.sick.service;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.ApplicationListRequest;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.ApplicationListResponse;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.api.model.response.RatesResponse;

import java.util.List;

public interface GeneralFormService {
    List<GeneralAllFormsResponse> selectAllApplicationsByPage(long pageNumber);
    GeneralFormsResponse getApplicationById(long id)  throws ApplicationNotFoundException;
    void createApplication(GeneralFormsRequest generalFormsRequest);
    PersonalInformationResponse getPersonalInformationById(long id) throws ApplicationNotFoundException;
    LeaseResponse getLeaseInformationById(long id) throws ApplicationNotFoundException;
    RatesResponse getRatesInformationById(long id) throws ApplicationNotFoundException;
    List<ApplicationListResponse> sortApplications(ApplicationListRequest applicationListRequest);
}
