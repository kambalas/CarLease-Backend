package com.example.sick.api.controller;

import com.example.sick.api.model.exception.StatusNotFoundException;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.service.StatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class StatusController {

    StatusServiceImpl statusService;

    @Autowired
    public StatusController(StatusServiceImpl statusService) {
        this.statusService = statusService;
    }

    @PatchMapping("/status/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody StatusRequest statusRequest) {
        statusService.updateStatusById(statusRequest);
    }

    @GetMapping("/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse getStatusByID(@PathVariable long id) throws StatusNotFoundException {
        return statusService.getStatusById(id);
    }

}
