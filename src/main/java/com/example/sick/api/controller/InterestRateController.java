package com.example.sick.api.controller;

import com.example.sick.api.model.request.InterestRateUpdateRequest;
import com.example.sick.service.InterestRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterestRateController {

    private final InterestRateService interestRateService;

    @Autowired
    public InterestRateController(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    @PatchMapping("/interest-rate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateInterestRateByType(@RequestBody InterestRateUpdateRequest request) {
        interestRateService.updateInterestRate(request);
        return ResponseEntity.ok().build();
    }
}
