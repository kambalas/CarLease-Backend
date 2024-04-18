package com.example.sick.utils.jwt;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarAPIJwtMapper implements RowMapper<CarAPIJwt> {
    @Override
    public CarAPIJwt mapRow(ResultSet rs, int rowNum) throws SQLException {
        CarAPIJwt jwtToken = new CarAPIJwt();
        jwtToken.setJwt(rs.getString("jwt"));
        jwtToken.setExpiresAt(rs.getLong("expires_at"));
        return jwtToken;
    }
}