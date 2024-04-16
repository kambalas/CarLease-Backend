package com.example.sick.repository;

import com.example.sick.api.model.request.LeaseAndRatesDAORequest;
import com.example.sick.api.model.response.LeaseAndRatesDAOResponse;

import java.util.List;
import java.util.Optional;

public interface LeaseAndRatesRepositoryInterface {

    public List<LeaseAndRatesDAOResponse> getAllLeaseAndRates();

    public Optional<LeaseAndRatesDAOResponse> getLeaseAndRateById(long pid);

    public void createLeaseAndRate(LeaseAndRatesDAORequest leaseAndRatesDAOResponse, long pid);
}
