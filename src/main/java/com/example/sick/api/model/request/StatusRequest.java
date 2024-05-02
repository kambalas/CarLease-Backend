package com.example.sick.api.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(

        @NotNull(message = "Id cannot be null")
        @Min(value = 1,message = "Id must be greater than 0")
        Long id,

        @NotNull(message = "Application status cannot be null")
        @NotBlank(message = "Application status cannot be blank")
        String APPLICATIONSTATUS
)
{ }
