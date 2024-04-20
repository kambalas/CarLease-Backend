package com.example.sick.repository;

import com.example.sick.domain.CarEngineDataAPIResponse;
import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import org.springframework.http.HttpHeaders;

public interface CarInfoRepositoryInterface {

    CarMakeAPIResponse getCarMakes(HttpHeaders headers);

    CarModelAPIResponse getCarModels(HttpHeaders headers, String make);

    CarEngineDataAPIResponse getEngineData (HttpHeaders headers, int modelID);


}