package com.example.sick.api.model.response;


public record GeneralFormsResponse(

        RatesResponse ratesResponse,
        PersonalInformationResponse personalInformationResponse,
        LeaseResponse leaseResponse

) {
}
