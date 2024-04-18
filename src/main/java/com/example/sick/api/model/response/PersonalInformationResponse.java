package com.example.sick.api.model.response;

import java.math.BigDecimal;
import java.util.Date;

public record PersonalInformationResponse(

        long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String pid,
        Date dateOfBirth,
        String maritalStatus,
        int numberOfChildren,
        String citizenship,
        BigDecimal monthlyIncome

) {
}
