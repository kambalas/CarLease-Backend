package com.example.sick.repository.mapper;

import com.example.sick.domain.ApplicationListDAOResponse;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ApplicationListMapper implements RowMapper<ApplicationListDAOResponse>{
    @Override
    public ApplicationListDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ApplicationListDAOResponse(
                resultSet.getLong("id"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getBoolean("isOpened"),
                ApplicationStatus.valueOf(resultSet.getString("status")),
                resultSet.getObject("updatedAt", LocalDateTime.class),
                resultSet.getBoolean("isHighRisk")
        );
    }
}
