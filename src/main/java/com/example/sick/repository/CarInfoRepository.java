package com.example.sick.repository;

import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.domain.EngineDataAPIResponse;
import com.example.sick.service.CarAPILoginService;
import com.example.sick.utils.jwt.CarAPIJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class CarInfoRepository implements CarInfoRepositoryInterface {

    private final RestTemplate restTemplate;
    private final CarAPIJwtRepository jwtTokenRepository;
    private final CarAPILoginService carAPILoginService;

    @Autowired
    public CarInfoRepository(RestTemplateBuilder restTemplateBuilder, CarAPIJwtRepository jwtTokenRepository, CarAPILoginService carAPILoginService) {
        this.restTemplate = restTemplateBuilder.build();
        this.jwtTokenRepository = jwtTokenRepository;
        this.carAPILoginService = carAPILoginService;
    }

    public CarMakeAPIResponse getCarMakes() throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();
        ResponseEntity<CarMakeAPIResponse> makesResponse = restTemplate.exchange(
                "https://carapi.app/api/makes?year=2019",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarMakeAPIResponse.class
        );
        return makesResponse.getBody();
    }

    public CarModelAPIResponse getCarModels(String make) throws JsonProcessingException {
        make = make.trim();
        String url = String.format("https://carapi.app/api/models?year=2019&make=%s", make);
        HttpHeaders headers = getHttpHeaders();
        ResponseEntity<CarModelAPIResponse> modelResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarModelAPIResponse.class
        );
        return modelResponse.getBody();
    }

    public EngineDataAPIResponse getModelEngineData(int modelID) throws JsonProcessingException {
        String url = String.format("https://carapi.app/api/engines?verbose=yes&make_model_id=%d&year=2019", modelID);
        HttpHeaders headers = getHttpHeaders();
        ResponseEntity<EngineDataAPIResponse> modelInfoResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EngineDataAPIResponse.class
        );
        return modelInfoResponse.getBody();
    }

    public EngineDataAPIResponse getVariantEngineData(int variantID) throws JsonProcessingException {
        String url = String.format("https://carapi.app/api/engines?verbose=yes&make_model_trim_id=%d&year=2019", variantID);
        HttpHeaders headers = getHttpHeaders();
        ResponseEntity<EngineDataAPIResponse> variantInfoResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EngineDataAPIResponse.class
        );
        return variantInfoResponse.getBody();
    }

    private CarAPIJwt getCarAPIJwt() throws JsonProcessingException {
        CarAPIJwt jwtToken = jwtTokenRepository.getJwtToken();
        if (jwtToken.isExpired()) {
            return carAPILoginService.loginAndSetJwt();
        }
        return jwtToken;
    }

    private HttpHeaders getHttpHeaders() throws JsonProcessingException {
        CarAPIJwt jwtToken = getCarAPIJwt();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(jwtToken.jwt());
        return headers;
    }
}