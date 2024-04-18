package com.example.sick.repository.mapper;

import com.example.sick.domain.PersonalInformationDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalInformationMapper implements RowMapper<PersonalInformationDAOResponse> {
    @Override
    public PersonalInformationDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new PersonalInformationDAOResponse(
                resultSet.getLong("id"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email"),
                resultSet.getString("phoneNumber"),
                resultSet.getString("pid"),
                resultSet.getDate("dateOfBirth"),
                resultSet.getString("maritalStatus"),
                resultSet.getInt("numberOfChildren"),
                resultSet.getString("citizenship"),
                resultSet.getBigDecimal("monthlyIncome")
        );
    }
}
