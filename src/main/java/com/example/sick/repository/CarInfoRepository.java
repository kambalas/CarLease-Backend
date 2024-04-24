package com.example.sick.repository;

import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.domain.EngineDataAPIResponse;
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

    @Autowired
    public CarInfoRepository(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CarMakeAPIResponse getCarMakes(HttpHeaders headers) {
        ResponseEntity<CarMakeAPIResponse> makesResponse = restTemplate.exchange(
                "https://carapi.app/api/makes?year=2019",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarMakeAPIResponse.class
        );
        return makesResponse.getBody();
    }

    public CarModelAPIResponse getCarModels(HttpHeaders headers, String make) {
        make = make.trim();
        String url = String.format("https://carapi.app/api/models?year=2019&make=%s", make);
        ResponseEntity<CarModelAPIResponse> modelResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarModelAPIResponse.class
        );
        return modelResponse.getBody();
    }

    public EngineDataAPIResponse getModelEngineData(HttpHeaders headers, int modelID) {
        String url = String.format("https://carapi.app/api/engines?verbose=yes&make_model_id=%d&year=2019", modelID);
        ResponseEntity<EngineDataAPIResponse> modelInfoResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EngineDataAPIResponse.class
        );
        return modelInfoResponse.getBody();
    }

    public EngineDataAPIResponse getVariantEngineData(HttpHeaders headers, int variantID) {
        String url = String.format("https://carapi.app/api/engines?verbose=yes&make_model_trim_id=%d&year=2019", variantID);
        ResponseEntity<EngineDataAPIResponse> variantInfoResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EngineDataAPIResponse.class
        );
        return variantInfoResponse.getBody();
    }
}