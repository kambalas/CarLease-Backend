package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.service.GeneralFormService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class LeaseController {

    GeneralFormService generalFormsService;

    public LeaseController(GeneralFormService generalFormsService) {
        this.generalFormsService = generalFormsService;
    }

    @GetMapping("/admin/lease/{id}")
    public LeaseResponse getLeaseById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getLeaseInformationById(id);
    }
}
