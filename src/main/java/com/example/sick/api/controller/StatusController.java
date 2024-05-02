package com.example.sick.api.controller;

import com.example.sick.api.model.exception.StatusNotFoundException;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.service.StatusServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class StatusController {

    private final StatusServiceImpl statusService;

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

    @PatchMapping("/status/update/is-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatusById(@RequestParam long id) {
        statusService.updateStatusIsRead(id);
    }

}
