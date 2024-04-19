package com.example.sick.api.controller;

import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.service.CarInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/carApi")
public class CarInfoController {

    CarInfoService carInfoService;

    @Autowired
    public CarInfoController(CarInfoService carInfoService) {
        this.carInfoService = carInfoService;
    }

    @GetMapping("/makes")
    @ResponseStatus(HttpStatus.OK)
    public CarMakeResponse getCarMakes() throws JsonProcessingException {
        return carInfoService.getCarMakes();
    }
}
