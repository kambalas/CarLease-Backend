package com.example.sick.repository.mapper;


import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements RowMapper<StatusDAOResponse> {
    @Override
    public StatusDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new StatusDAOResponse(
                resultSet.getLong("id"),
                ApplicationStatus.valueOf(resultSet.getString("status")),
                resultSet.getBoolean("isOpened"),
                resultSet.getTimestamp("updatedAt"),
                resultSet.getTimestamp("createdAt"),
                resultSet.getBoolean("isHighRisk")
                );
    }
}

