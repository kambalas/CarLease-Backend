package com.example.sick.repository.mapper;

import com.example.sick.domain.AuthenticationDAORequest;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<AuthenticationDAORequest> {
  public AuthenticationDAORequest mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return new AuthenticationDAORequest(
            resultSet.getString("username"),
            resultSet.getString("password")
    );
  }
}
