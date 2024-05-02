package com.example.sick.repository.mapper;

import com.example.sick.domain.ApplicationMonthlyCountDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationMonthlyCountMapper implements RowMapper<ApplicationMonthlyCountDAOResponse> {
    @Override
    public ApplicationMonthlyCountDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ApplicationMonthlyCountDAOResponse(
                resultSet.getInt("thisMonthCount"),
                resultSet.getInt("previousMonthCount")
        );
    }
}
