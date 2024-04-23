package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.response.RatesResponse;
import com.example.sick.service.GeneralFormServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class RatesController {

    private final GeneralFormServiceImpl generalFormsService;

    public RatesController(GeneralFormServiceImpl generalFormsService) {
        this.generalFormsService = generalFormsService;
    }

    @GetMapping("/rates/{id}")
    public RatesResponse getRatesById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getRatesInformationById(id);
    }
}
