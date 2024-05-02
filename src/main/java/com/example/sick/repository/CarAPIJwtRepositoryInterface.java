package com.example.sick.repository;

import com.example.sick.utils.jwt.CarAPIJwt;

public interface CarAPIJwtRepositoryInterface {

    CarAPIJwt getJwtToken();

    void updateJwtToken(CarAPIJwt jwtToken);
}
