package com.example.sick.repository.mapper;

import com.example.sick.domain.AcceptedApplicationDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcceptedApplicationMapper implements RowMapper<AcceptedApplicationDAOResponse> {
    @Override
    public AcceptedApplicationDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new AcceptedApplicationDAOResponse(
                resultSet.getBigDecimal("thisYearTotalPayments"),
                resultSet.getBigDecimal("lastYearTotalPayments")
        );
    }
}

