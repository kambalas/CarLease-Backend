package com.example.sick.api.model.request;

import com.example.sick.utils.ApplicationStatus;

public record StatusRequest(
        long id,
        String APPLICATIONSTATUS
)
{ }
