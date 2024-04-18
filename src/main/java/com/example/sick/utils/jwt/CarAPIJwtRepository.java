package com.example.sick.utils.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class CarAPIJwtRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CarAPIJwtRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public CarAPIJwt getJwtToken() {
        String query = "SELECT * FROM JWT";
        return namedParameterJdbcTemplate.queryForObject(query, new HashMap<>(), new CarAPIJwtMapper());
    }

    public void updateJwtToken(CarAPIJwt jwtToken) {
        String query = "UPDATE JWT SET jwt = :jwt, expires_at = :expiresAt";
        namedParameterJdbcTemplate.update(query, new BeanPropertySqlParameterSource(jwtToken));
    }
}



