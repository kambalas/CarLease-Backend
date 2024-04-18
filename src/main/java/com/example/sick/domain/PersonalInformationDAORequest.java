package com.example.sick.domain;

import java.math.BigDecimal;
import java.util.Date;

public record PersonalInformationDAORequest(

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
