package com.example.sick.domain;

import com.example.sick.utils.ApplicationStatus;


public record StatusDAORequest(

        long id,
        ApplicationStatus APPLICATIONSTATUS
){
}
