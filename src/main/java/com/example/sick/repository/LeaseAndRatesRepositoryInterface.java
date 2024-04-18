package com.example.sick.repository;

import com.example.sick.domain.LeaseAndRatesDAORequest;
import com.example.sick.domain.LeaseAndRatesDAOResponse;

import java.util.List;
import java.util.Optional;

public interface LeaseAndRatesRepositoryInterface {

    public List<LeaseAndRatesDAOResponse> getAllLeaseAndRates();

    public Optional<LeaseAndRatesDAOResponse> getLeaseAndRateById(long pid);

    public void createLeaseAndRate(LeaseAndRatesDAORequest leaseAndRatesDAOResponse, long pid);

    List<LeaseAndRatesDAOResponse> getAllLeaseAndRatesByPage(long pageNumber);
}
