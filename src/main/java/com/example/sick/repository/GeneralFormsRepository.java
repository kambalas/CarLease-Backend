package com.example.sick.repository;

import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.repository.inteface.GeneralFormsInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Repository
public class GeneralFormsRepository implements GeneralFormsInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GeneralFormsRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    @Override
    public List<GeneralFormsResponse> getAllGeneralForms() {
        return null;
    }

    @Override
    public Optional<GeneralFormsResponse> getGeneralFormById(BigInteger id) {
        return null;
    }

}
