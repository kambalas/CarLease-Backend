package com.example.sick.api.model.request;

import java.math.BigDecimal;
import java.util.Date;

public record PersonalInformationRequest(

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
