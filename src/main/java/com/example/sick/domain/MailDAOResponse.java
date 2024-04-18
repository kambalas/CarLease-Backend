package com.example.sick.domain;

public record MailDAOResponse (
        long id,
        long applicationId,
        String mailText
){
}
