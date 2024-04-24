package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.service.GeneralFormService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class LeaseController {

    private final GeneralFormService generalFormsService;

    public LeaseController( GeneralFormService generalFormsService) {
      this.generalFormsService = generalFormsService;
    }

    @GetMapping("/lease/{id}")
    public LeaseResponse getLeaseById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getLeaseInformationById(id);
    }
}
