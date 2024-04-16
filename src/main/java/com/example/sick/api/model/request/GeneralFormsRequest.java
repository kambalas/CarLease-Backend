package com.example.sick.api.model.request;



public record GeneralFormsRequest(
        RatesRequest ratesRequest,
        PersonalInformationRequest personalInformationRequest,
        LeaseRequest leaseRequest
) {
}
