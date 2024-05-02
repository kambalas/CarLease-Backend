package com.example.sick.api.model.response;

public record LeaseAndRatesResponse(

        LeaseResponse leaseResponse,
        RatesResponse ratesResponse

) {
}
