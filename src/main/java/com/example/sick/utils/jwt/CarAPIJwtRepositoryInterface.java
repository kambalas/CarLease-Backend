package com.example.sick.utils.jwt;

public interface CarAPIJwtRepositoryInterface {

    CarAPIJwt getJwtToken();

    void updateJwtToken(CarAPIJwt jwtToken);
}
