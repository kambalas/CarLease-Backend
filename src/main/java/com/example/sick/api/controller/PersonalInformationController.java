package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.service.GeneralFormServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class PersonalInformationController {

    GeneralFormServiceImpl generalFormsService;

    public PersonalInformationController(GeneralFormServiceImpl generalFormsService) {
        this.generalFormsService = generalFormsService;
    }

    @GetMapping("/personal-information/{id}")
    public PersonalInformationResponse getPersonalInformationById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getPersonalInformationById(id);
    }
}
