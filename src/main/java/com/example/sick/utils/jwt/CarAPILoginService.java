package com.example.sick.utils.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.jwt.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class CarAPILoginService {

    private final RestTemplate restTemplate;
    private final CarAPIJwtRepository jwtRepository;

    public CarAPILoginService(RestTemplate restTemplate, CarAPIJwtRepository jwtRepository) {
        this.restTemplate = restTemplate;
        this.jwtRepository = jwtRepository;
    }

    public CarAPIJwt loginAndSetJwt() throws JsonProcessingException {

        Map<String, String> loginRequestBody = new HashMap<>();
        String apiToken = "72e04517-cfa7-4dfb-acdd-e3c46470d7eb";
        loginRequestBody.put("api_token", apiToken);
        String apiSecret = "29c80960b12f2e6f93c99b50ce3c3682";
        loginRequestBody.put("api_secret", apiSecret);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "https://carapi.app/api/auth/login",
                loginRequestBody,
                String.class
        );

        String jwt = loginResponse.getBody();
        Long expiresAt = getExpiresAt(jwt);
        CarAPIJwt updatedJwt = new CarAPIJwt(jwt, expiresAt);

        jwtRepository.updateJwtToken(updatedJwt);
        return updatedJwt;
    }

    private static Long getExpiresAt(String jwt) throws JsonProcessingException {
        String claims = JwtHelper.decode(jwt).getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claimsMap = objectMapper.readValue(claims, new TypeReference<>() {
        });
        return (Long) claimsMap.get("exp");
    }
}
