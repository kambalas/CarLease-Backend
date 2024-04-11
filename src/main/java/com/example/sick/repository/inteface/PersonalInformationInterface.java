package com.example.sick.repository.inteface;

import com.example.sick.api.model.response.PersonalInformationResponse;

import java.util.List;
import java.util.Optional;

public interface PersonalInformationInterface {

    List<PersonalInformationResponse> getAllPersonalInformation();

    Optional<PersonalInformationResponse> getAllPersonalInformationById(Long id);
}
