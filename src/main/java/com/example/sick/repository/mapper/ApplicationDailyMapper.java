package com.example.sick.repository.mapper;

import com.example.sick.domain.ApplicationDailyCountDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationDailyMapper implements RowMapper<ApplicationDailyCountDAOResponse> {
    @Override
    public ApplicationDailyCountDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ApplicationDailyCountDAOResponse(
                resultSet.getTimestamp("day").toLocalDateTime(),
                resultSet.getInt("applicationCount")
        );
    }
}
