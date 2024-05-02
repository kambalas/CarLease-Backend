package com.example.sick.repository;

import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.domain.EngineDataAPIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CarInfoRepositoryInterface {

    CarMakeAPIResponse getCarMakes() throws JsonProcessingException;

    CarModelAPIResponse getCarModels(String make) throws JsonProcessingException;

    EngineDataAPIResponse getModelEngineData(int modelID) throws JsonProcessingException;

    EngineDataAPIResponse getVariantEngineData(int variantID) throws JsonProcessingException;

}