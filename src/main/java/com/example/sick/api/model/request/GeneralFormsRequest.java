package com.example.sick.api.model.request;

public record GeneralFormsRequest(

        LeaseRequest leaseRequest,
        CalculatorRequest calculatorRequest,
        RatesRequest ratesRequest
) {
}
