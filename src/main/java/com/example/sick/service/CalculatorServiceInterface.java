package com.example.sick.service;

import com.example.sick.api.model.request.CalculatorRequest;
import com.example.sick.api.model.response.CalculatorResponse;

import java.math.BigDecimal;

public interface CalculatorServiceInterface {
    CalculatorResponse calculateMonthlyPayment(CalculatorRequest calculatorRequest);
}
