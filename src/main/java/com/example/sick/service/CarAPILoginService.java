package com.example.sick.service;

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

    @Autowired
    public CarAPILoginService(RestTemplate restTemplate, CarAPIJwtRepository jwtRepository) {
        this.restTemplate = restTemplate;
        this.jwtRepository = jwtRepository;
    }

    public CarAPIJwt loginAndSetJwt() throws JsonProcessingException {

        Map<String, String> loginRequestBody = new HashMap<>();
        String carApiToken = "29c80960b12f2e6f93c99b50ce3c3682";
        loginRequestBody.put("api_token", carApiToken);
        String carApiSecret = "72e04517-cfa7-4dfb-acdd-e3c46470d7eb";
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
