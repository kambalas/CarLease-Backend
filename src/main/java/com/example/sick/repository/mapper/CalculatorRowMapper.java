package com.example.sick.repository.mapper;

import com.example.sick.domain.CalculatorDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculatorRowMapper implements RowMapper<CalculatorDAOResponse> {
    @Override
    public CalculatorDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CalculatorDAOResponse(
                resultSet.getDouble("regular"),
                resultSet.getDouble("eco")
        );
    }
}