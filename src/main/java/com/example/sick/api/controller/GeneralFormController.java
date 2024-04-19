package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.service.GeneralFormServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class GeneralFormController {

    GeneralFormServiceImpl generalFormsService;

    @Autowired
    public GeneralFormController(GeneralFormServiceImpl generalFormsService) {
        this.generalFormsService = generalFormsService;
    }

    @GetMapping("/applications/page/{pageNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<GeneralAllFormsResponse> getAllApplicationsByPage(@PathVariable long pageNumber) {
        return generalFormsService.selectAllApplicationsByPage(pageNumber);
    }


    @GetMapping("/applications/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GeneralFormsResponse getApplicationById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getApplicationById(id);
    }


    @PostMapping("/applications/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createApplication(@RequestBody GeneralFormsRequest generalFormsRequest) throws IllegalArgumentException {
        generalFormsService.createApplication(generalFormsRequest);
    }

}
