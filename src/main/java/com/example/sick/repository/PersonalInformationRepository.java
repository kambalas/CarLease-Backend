package com.example.sick.repository;

import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.repository.inteface.PersonalInformationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonalInformationRepository implements PersonalInformationInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PersonalInformationRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public List<PersonalInformationResponse> getAllPersonalInformation() {
        return null;
    }

    public Optional<PersonalInformationResponse> getAllPersonalInformationById(Long id) {
        return null;
    }


}
