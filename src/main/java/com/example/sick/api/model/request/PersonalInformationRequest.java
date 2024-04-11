package com.example.sick.api.model.request;

import java.math.BigDecimal;

public record PersonalInformationRequest(

        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        long pid,
        String dateOfBirth,
        String maritalStatus,
        int numberOfChildren,
        String citizenship,
        BigDecimal monthlyIncome

) {
}
