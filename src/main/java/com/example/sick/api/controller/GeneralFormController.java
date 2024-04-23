package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.ApplicationListRequest;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.ApplicationListResponse;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.service.GeneralFormServiceImpl;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeneralFormController {

    private final GeneralFormServiceImpl generalFormsService;

    public GeneralFormController(GeneralFormServiceImpl generalFormsService) {
        this.generalFormsService = generalFormsService;
    }

    @GetMapping("/admin/applications/page/{pageNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<GeneralAllFormsResponse> getAllApplicationsByPage(@PathVariable long pageNumber) {
        return generalFormsService.selectAllApplicationsByPage(pageNumber);
    }


    @GetMapping("/admin/applications/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GeneralFormsResponse getApplicationById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getApplicationById(id);
    }


    @PostMapping("/user/applications/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createApplication(@RequestBody GeneralFormsRequest generalFormsRequest) throws IllegalArgumentException {
        generalFormsService.createApplication(generalFormsRequest);
    }

    @GetMapping("/admin/applications")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationListResponse> sortApplications(@RequestBody ApplicationListRequest applicationListRequest) {
        return generalFormsService.sortApplications(applicationListRequest);
    }

}
