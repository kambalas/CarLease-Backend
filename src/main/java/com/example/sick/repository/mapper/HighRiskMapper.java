package com.example.sick.repository.mapper;

import com.example.sick.domain.HighRiskDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HighRiskMapper implements RowMapper<HighRiskDAOResponse> {
    @Override
    public HighRiskDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new HighRiskDAOResponse(
                resultSet.getInt("currentMonthHighRiskCount"),
                resultSet.getInt("previousMonthHighRiskCount")
        );
    }
}
