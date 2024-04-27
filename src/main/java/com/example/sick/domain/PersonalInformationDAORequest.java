package com.example.sick.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record PersonalInformationDAORequest(

    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String pid,
    LocalDate dateOfBirth,
    String maritalStatus,
    int numberOfChildren,
    String citizenship,
    BigDecimal monthlyIncome

) {
}
