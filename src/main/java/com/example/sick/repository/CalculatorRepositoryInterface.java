package com.example.sick.repository;

import com.example.sick.domain.CalculatorDAOResponse;

import java.math.BigDecimal;

public interface CalculatorRepositoryInterface {

    CalculatorDAOResponse selectAllInterestRate();
}
