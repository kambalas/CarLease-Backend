package com.example.sick.repository;

import com.example.sick.domain.EngineDataAPIResponse;
import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import org.springframework.http.HttpHeaders;

public interface CarInfoRepositoryInterface {

    CarMakeAPIResponse getCarMakes(HttpHeaders headers);

    CarModelAPIResponse getCarModels(HttpHeaders headers, String make);

    EngineDataAPIResponse getModelEngineData(HttpHeaders headers, int modelID);

    EngineDataAPIResponse getVariantEngineData(HttpHeaders headers, int variantID);
}