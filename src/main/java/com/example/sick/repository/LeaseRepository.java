package com.example.sick.repository;

import com.example.sick.api.model.response.LeaseAndRatesResponse;
import com.example.sick.repository.inteface.LeaseRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class LeaseRepository implements LeaseRepositoryInterface{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LeaseRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    @Override
    public List<LeaseAndRatesResponse> getAllLeaseAndRates() {
        return null;
    }

    @Override
    public Optional<LeaseAndRatesResponse> getlAllLeaseAndRatesById(BigInteger id) {
        return null;
    }
}
