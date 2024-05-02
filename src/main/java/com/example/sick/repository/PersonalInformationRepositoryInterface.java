package com.example.sick.repository;

import com.example.sick.domain.PersonalInformationDAORequest;
import com.example.sick.domain.PersonalInformationDAOResponse;

import java.util.List;
import java.util.Optional;

public interface PersonalInformationRepositoryInterface {

    List<PersonalInformationDAOResponse> getAllPersonalInformation();

    List<PersonalInformationDAOResponse> getAllPersonalInformationByPage(long pageNumber);

    Optional<PersonalInformationDAOResponse> getPersonalInformationById(long pid);

    long createPersonalInformation(PersonalInformationDAORequest personalInformationRequest);
}
