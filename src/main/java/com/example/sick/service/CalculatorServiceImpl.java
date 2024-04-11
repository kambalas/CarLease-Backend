package com.example.sick.service;

import com.example.sick.api.model.request.CalculatorRequest;
import com.example.sick.repository.CalculatorMapper;
import com.example.sick.repository.CalculatorRepository;
import com.example.sick.repository.CalculatorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    private final CalculatorService calculatorService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Calculator> findAll() {
        String SQL = "SELECT * FROM task";
        return jdbcTemplate.query(SQL, new CalculatorMapper());
    }

    @Autowired
    public CalculatorServiceImpl(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public BigDecimal calculateMonthlyPayment(CalculatorRequest calculatorRequest) {

//        BigDecimal interestRate = BigDecimal.valueOf(calculatorRequest.isEcoFriendly() ?
//                calculatorService.findInterestRateByCarType("eco-friendly") :
//                calculatorService.findInterestRateByCarType("regular"));

        BigDecimal interestRate = BigDecimal.valueOf(5);
        BigDecimal rate = interestRate.divide(BigDecimal.valueOf(100));

        BigDecimal totalPayableInterest = (calculatorRequest.carValue().subtract(calculatorRequest.downPayment())
                .subtract(calculateResidualValue(calculatorRequest.carValue(), calculatorRequest.residualValuePercentage())))
                .multiply(rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)) // Assuming 2 decimal places for monthly rate
                .multiply(BigDecimal.valueOf(calculatorRequest.period()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP));

        BigDecimal monthlyInterest = totalPayableInterest.divide(BigDecimal.valueOf(calculatorRequest.period()), 2, RoundingMode.HALF_UP);

        BigDecimal monthlyLoanAmount = (calculatorRequest.carValue().subtract(calculatorRequest.downPayment())
                .subtract(calculateResidualValue(calculatorRequest.carValue(), calculatorRequest.residualValuePercentage())))
                .divide(BigDecimal.valueOf(calculatorRequest.period()), 2, RoundingMode.HALF_UP);

        return monthlyLoanAmount.add(monthlyInterest);
    }

    private BigDecimal calculateResidualValue(BigDecimal carValue, int residualValuePercentage) {
        BigDecimal percentage = BigDecimal.valueOf(residualValuePercentage).divide(BigDecimal.valueOf(100));
        return carValue.multiply(percentage);
    }

    private void saveMonthlyPayment(CalculatorRequest calculatorRequest, BigDecimal monthlyPayment) {
        calculatorRepository.saveMonthlyPayment(calculatorRequest, monthlyPayment);
    }

}
