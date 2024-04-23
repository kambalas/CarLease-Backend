package com.example.sick.domain;

import java.util.List;
import java.util.Optional;

public record ApplicationListDAORequest (
        long page,
        List<String> statuses,
        String searchQuery
){

}