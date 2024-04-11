package com.example.sick.service;

import com.example.sick.api.model.request.InterestRateUpdateRequest;
import com.example.sick.repository.CalculatorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InterestRateService {

    private final CalculatorServiceImp calculatorServiceImp;

    @Autowired
    public InterestRateService(CalculatorServiceImp calculatorServiceImp) {
        this.calculatorServiceImp = calculatorServiceImp;
    }

    public void updateInterestRate(InterestRateUpdateRequest request) {
        calculatorServiceImp.updateInterestRate(request.carType(), request.rate());
    }

}
