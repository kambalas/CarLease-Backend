package com.example.sick.service;

import com.example.sick.api.model.request.CalculatorRequest;
import com.example.sick.api.model.response.CalculatorResponse;
import com.example.sick.domain.CalculatorDAOResponse;
import com.example.sick.repository.CalculatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class CalculatorService implements CalculatorServiceInterface {

    private final CalculatorRepository calculatorRepository;

    @Autowired
    public CalculatorService(CalculatorRepository calculatorRepository) {
        this.calculatorRepository = calculatorRepository;
    }

    public CalculatorResponse calculateMonthlyPayment(CalculatorRequest calculatorRequest) {

        CalculatorDAOResponse calculatorDAOResponse = calculatorRepository.selectAllInterestRate();

        BigDecimal interestRate = BigDecimal.valueOf(calculatorRequest.isEcoFriendly() ?
                calculatorDAOResponse.eco() :
                calculatorDAOResponse.regular());

        BigDecimal rate = interestRate.divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

        BigDecimal residualValue = calculatorRequest.carValue()
                .multiply(BigDecimal.valueOf(calculatorRequest.residualValuePercentage()))
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

        BigDecimal loanAmountMinusDownPayment = calculatorRequest.carValue()
                .subtract(calculatorRequest.downPayment());

        BigDecimal totalPayableInterest = loanAmountMinusDownPayment
                .multiply(rate)
                .multiply(BigDecimal.valueOf(calculatorRequest.period()))
                .divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);

        BigDecimal monthlyInterest = totalPayableInterest.divide(BigDecimal.valueOf(calculatorRequest.period()), MathContext.DECIMAL128);

        BigDecimal monthlyLoanAmount = loanAmountMinusDownPayment
                .subtract(residualValue)
                .divide(BigDecimal.valueOf(calculatorRequest.period()), MathContext.DECIMAL128);

        BigDecimal monthlyPayment = monthlyLoanAmount.add(monthlyInterest);

        return new CalculatorResponse(monthlyPayment.setScale(2, RoundingMode.HALF_UP));
    }
}
