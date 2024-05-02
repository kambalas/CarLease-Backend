package com.example.sick.repository.mapper;


import com.example.sick.domain.StatusCountDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusCountMapper implements RowMapper<StatusCountDAOResponse> {
    @Override
    public StatusCountDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new StatusCountDAOResponse(
                resultSet.getInt("newCount"),
                resultSet.getInt("acceptedCount"),
                resultSet.getInt("rejectedCount"),
                resultSet.getInt("pendingCount")
        );
    }
}
