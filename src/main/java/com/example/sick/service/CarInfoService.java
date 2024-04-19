package com.example.sick.service;

import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.repository.CarAPIJwtRepository;
import com.example.sick.utils.jwt.CarAPIJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarInfoService {
    private RestTemplate restTemplate;
    private CarAPIJwtRepository jwtTokenRepository;
    private CarAPILoginService carAPILoginService;


    public CarInfoService(RestTemplateBuilder restTemplateBuilder,
                          CarAPIJwtRepository jwtTokenRepository,CarAPILoginService carAPILoginService) {
        this.carAPILoginService = carAPILoginService;
        this.restTemplate = restTemplateBuilder.build();
        this.jwtTokenRepository = jwtTokenRepository;
    }

    public CarMakeResponse getCarMakes() throws JsonProcessingException {
        CarAPIJwt jwtToken = getCarAPIJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken.jwt());

        ResponseEntity<CarMakeAPIResponse> makesResponse = restTemplate.exchange(
                "https://carapi.app/api/makes",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarMakeAPIResponse.class
        );

        List<String> carMakes = Objects.requireNonNull(makesResponse.getBody()).data().stream()
                .map(carMake -> (String) carMake.get("name"))
                .collect(Collectors.toList());

        return new CarMakeResponse(carMakes);
    }

    private CarAPIJwt getCarAPIJwt() throws JsonProcessingException {
        CarAPIJwt jwtToken = jwtTokenRepository.getJwtToken();
        if (jwtToken.isExpired()) {
            return carAPILoginService.loginAndSetJwt();
        }
        return jwtToken;
    }
}
