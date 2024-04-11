package com.example.sick.repository.inteface;

import com.example.sick.api.model.response.LeaseAndRatesResponse;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface LeaseRepositoryInterface {

    public List<LeaseAndRatesResponse> getAllLeaseAndRates();

    public Optional<LeaseAndRatesResponse> getlAllLeaseAndRatesById(BigInteger id);

}
