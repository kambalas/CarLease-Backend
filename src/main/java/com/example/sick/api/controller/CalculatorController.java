package com.example.sick.api.controller;

import com.example.sick.api.model.request.CalculatorRequest;
import com.example.sick.api.model.response.CalculatorResponse;
import com.example.sick.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculator")
    @ResponseStatus(HttpStatus.OK)
    public CalculatorResponse calculate(@RequestBody CalculatorRequest calculatorRequest) {
        return calculatorService.calculateMonthlyPayment(calculatorRequest);
    }

}
