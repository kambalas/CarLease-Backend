package com.example.sick.service;

import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.api.model.response.CarModelInfoResponse;
import com.example.sick.api.model.response.CarModelResponse;
import com.example.sick.api.model.response.CarVariantInfoResponse;
import com.example.sick.domain.EngineDataAPIResponse;
import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.repository.CarAPIJwtRepository;
import com.example.sick.repository.CarInfoRepository;
import com.example.sick.repository.mapper.CarModelInfoMapper;
import com.example.sick.repository.mapper.CarVariantInfoMapper;
import com.example.sick.utils.jwt.CarAPIJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarInfoService {

    private final CarAPIJwtRepository jwtTokenRepository;
    private final CarInfoRepository carInfoRepository;
    private final CarAPILoginService carAPILoginService;


    @Autowired
    public CarInfoService(CarAPIJwtRepository jwtTokenRepository,
                          CarInfoRepository carInfoRepository,
                          CarAPILoginService carAPILoginService) {
        this.carInfoRepository = carInfoRepository;
        this.carAPILoginService = carAPILoginService;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    public CarMakeResponse getCarMakes() throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();
        CarMakeAPIResponse makesResponse = carInfoRepository.getCarMakes(headers);

        List<String> carMakes = Objects.requireNonNull(makesResponse).data().stream()
                .map(carMake -> (String) carMake.get("name"))
                .collect(Collectors.toList());

        return new CarMakeResponse(carMakes);
    }

    public CarModelResponse getModelsForMake(String make) throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();
        CarModelAPIResponse modelResponse = carInfoRepository.getCarModels(headers, make);

        List<CarModelResponse.CarModel> carModels = Objects.requireNonNull(modelResponse).data().stream()
                .map(modelData -> new CarModelResponse.CarModel(modelData.id(), modelData.name()))
                .collect(Collectors.toList());

        return new CarModelResponse(carModels);
    }

    public CarModelInfoResponse getModelInfo(int modelID) throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();
        EngineDataAPIResponse modelEngineResponse = carInfoRepository.getModelEngineData(headers, modelID);
        CarModelInfoMapper mapper = new CarModelInfoMapper();

        return mapper.mapFrom(modelEngineResponse);
    }

    public CarVariantInfoResponse getVariantInfo(int variantID) throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();
        EngineDataAPIResponse variantEngineResponse = carInfoRepository.getVariantEngineData(headers, variantID);
        CarVariantInfoMapper mapper = new CarVariantInfoMapper();

        return mapper.mapFrom(variantEngineResponse);
    }

    private HttpHeaders getHttpHeaders() throws JsonProcessingException {
        CarAPIJwt jwtToken = getCarAPIJwt();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(jwtToken.jwt());
        return headers;
    }

    private CarAPIJwt getCarAPIJwt() throws JsonProcessingException {
        CarAPIJwt jwtToken = jwtTokenRepository.getJwtToken();
        if (jwtToken.isExpired()) {
            return carAPILoginService.loginAndSetJwt();
        }
        return jwtToken;
    }
}
