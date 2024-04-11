package com.example.sick.api.model.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public record PersonalInformationResponse(

        BigInteger id,
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
