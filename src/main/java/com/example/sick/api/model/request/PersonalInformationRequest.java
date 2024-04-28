package com.example.sick.api.model.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PersonalInformationRequest(

        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String pid,
        LocalDateTime dateOfBirth,
        String maritalStatus,
        int numberOfChildren,
        String citizenship,
        BigDecimal monthlyIncome

) {
}
