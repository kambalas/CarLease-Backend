package com.example.sick.api.model.response;
import com.example.sick.api.model.request.LeaseRequest;
import com.example.sick.api.model.request.RatesRequest;
import com.example.sick.api.model.response.PersonalInformationResponse;


public record GeneralFormsResponse(

        RatesResponse ratesResponse,
        PersonalInformationResponse personalInformationResponse,
        LeaseResponse leaseResponse



) {
}
