package com.example.sick.service;

import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.api.model.response.CarModelInfoResponse;
import com.example.sick.api.model.response.CarModelResponse;
import com.example.sick.api.model.response.CarVariantInfoResponse;
import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.domain.EngineDataAPIResponse;
import com.example.sick.repository.CarInfoRepository;
import com.example.sick.repository.mapper.CarModelInfoMapper;
import com.example.sick.repository.mapper.CarVariantInfoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarInfoService {

    private final CarInfoRepository carInfoRepository;

    @Autowired
    public CarInfoService(CarInfoRepository carInfoRepository) {
        this.carInfoRepository = carInfoRepository;
    }

    public CarMakeResponse getCarMakes() throws JsonProcessingException {
        CarMakeAPIResponse makesResponse = carInfoRepository.getCarMakes();

        List<String> carMakes = Objects.requireNonNull(makesResponse).data().stream()
                .map(carMake -> (String) carMake.get("name"))
                .collect(Collectors.toList());

        return new CarMakeResponse(carMakes);
    }

    public CarModelResponse getModelsForMake(String make) throws JsonProcessingException {
        CarModelAPIResponse modelResponse = carInfoRepository.getCarModels(make);

        List<CarModelResponse.CarModel> carModels = Objects.requireNonNull(modelResponse).data().stream()
                .map(modelData -> new CarModelResponse.CarModel(modelData.id(), modelData.name()))
                .collect(Collectors.toList());

        return new CarModelResponse(carModels);
    }

    public CarModelInfoResponse getModelInfo(int modelID) throws JsonProcessingException {
        EngineDataAPIResponse modelEngineResponse = carInfoRepository.getModelEngineData(modelID);
        CarModelInfoMapper mapper = new CarModelInfoMapper();

        return mapper.mapFrom(modelEngineResponse);
    }

    public CarVariantInfoResponse getVariantInfo(int variantID) throws JsonProcessingException {
        EngineDataAPIResponse variantEngineResponse = carInfoRepository.getVariantEngineData(variantID);
        CarVariantInfoMapper mapper = new CarVariantInfoMapper();

        return mapper.mapFrom(variantEngineResponse);
    }
}
