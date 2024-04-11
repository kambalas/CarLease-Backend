package com.example.sick.api.model.response;

import com.example.sick.api.model.request.CalculatorRequest;
import com.example.sick.api.model.request.LeaseRequest;
import com.example.sick.api.model.request.RatesRequest;


public record GeneralFormsResponse(

        LeaseRequest leaseResponse,
        CalculatorRequest calculatorResponse,
        RatesRequest ratesResponse

) {
}
