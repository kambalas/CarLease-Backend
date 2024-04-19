package com.example.sick.service;

import org.springframework.beans.factory.annotation.Value;
import com.example.sick.repository.CarAPIJwtRepository;
import com.example.sick.utils.jwt.CarAPIJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CarAPILoginService {

    private final RestTemplate restTemplate;
    private final CarAPIJwtRepository jwtRepository;

    @Value("${car.api.token}")
    private String carApiToken;

    @Value("${car.api.secret}")
    private String carApiSecret;


    public CarAPILoginService(RestTemplate restTemplate, CarAPIJwtRepository jwtRepository) {
        this.restTemplate = restTemplate;
        this.jwtRepository = jwtRepository;
    }

    public CarAPIJwt loginAndSetJwt() throws JsonProcessingException {

        Map<String, String> loginRequestBody = new HashMap<>();
        loginRequestBody.put("api_token", carApiToken);
        loginRequestBody.put("api_secret", carApiSecret);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "https://carapi.app/api/auth/login",
                loginRequestBody,
                String.class
        );

        String jwt = loginResponse.getBody();
        int expiresAt = getExpiresAt(jwt);
        CarAPIJwt updatedJwt = new CarAPIJwt(jwt, expiresAt);

        jwtRepository.updateJwtToken(updatedJwt);
        return updatedJwt;
    }

    private static int getExpiresAt(String jwt) throws JsonProcessingException {
        String claims = JwtHelper.decode(jwt).getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claimsMap = objectMapper.readValue(claims, new TypeReference<>() {
        });
        return (int) claimsMap.get("exp");
    }
}
