package com.example.sick.api.model.response;

public record GeneralAllFormsResponse (
    RatesResponse ratesResponse,
    PersonalInformationResponse personalInformationResponse,
    LeaseResponse leaseResponse,
    StatusResponse statusResponse
){}
