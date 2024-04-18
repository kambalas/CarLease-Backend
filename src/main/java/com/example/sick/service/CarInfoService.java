package com.example.sick.service;

import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.utils.jwt.CarAPIJwtRepository;
import com.example.sick.utils.jwt.CarAPIJwt;
import com.example.sick.utils.jwt.CarAPILoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarInfoService {
    private final RestTemplate restTemplate;
    private final CarAPIJwtRepository jwtTokenRepository;
    private final CarAPILoginService carAPILoginService;

    public CarInfoService(RestTemplateBuilder restTemplateBuilder,
                          CarAPIJwtRepository jwtTokenRepository,
                          CarAPILoginService carAPILoginService) {
        this.restTemplate = restTemplateBuilder.build();
        this.jwtTokenRepository = jwtTokenRepository;
        this.carAPILoginService = carAPILoginService;
    }

    public CarMakeResponse getCarMakes() throws JsonProcessingException {
        CarAPIJwt jwtToken = getCarAPIJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken.getJwt());

        ResponseEntity<CarMakeAPIResponse> makesResponse = restTemplate.exchange(
                "https://carapi.app/api/makes",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarMakeAPIResponse.class
        );

        List<String> carMakes = makesResponse.getBody().data().stream()
                .map(carMake -> (String) carMake.get("name"))
                .collect(Collectors.toList());

        return new CarMakeResponse(carMakes);
    }

    private CarAPIJwt getCarAPIJwt() throws JsonProcessingException {
        CarAPIJwt jwtToken = jwtTokenRepository.getJwtToken();
        if (jwtToken == null || jwtToken.isExpired()) {
            jwtToken = carAPILoginService.loginAndSetJwt();
        }
        return jwtToken;
    }
}
