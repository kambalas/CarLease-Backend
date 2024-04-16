package com.example.sick.api.model.response;

import java.math.BigDecimal;
import java.util.Date;

public record PersonalInformationDAOResponse(

        long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        long pid,
        Date dateOfBirth,
        String maritalStatus,
        int numberOfChildren,
        String citizenship,
        BigDecimal monthlyIncome

) {}
