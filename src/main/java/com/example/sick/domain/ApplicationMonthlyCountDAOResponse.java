package com.example.sick.domain;

public record ApplicationMonthlyCountDAOResponse(
        int thisMonthCount,
        int previousMonthCount
){
}
