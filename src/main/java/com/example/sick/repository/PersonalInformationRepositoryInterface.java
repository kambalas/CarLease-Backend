package com.example.sick.repository;

import com.example.sick.api.model.request.PersonalInformationDAORequest;
import com.example.sick.api.model.response.PersonalInformationDAOResponse;

import java.util.List;
import java.util.Optional;

public interface PersonalInformationRepositoryInterface {

    List<PersonalInformationDAOResponse> getAllPersonalInformation();

    Optional<PersonalInformationDAOResponse> getPersonalInformationById(long pid);

    long createPersonalInformation(PersonalInformationDAORequest personalInformationRequest);
}
