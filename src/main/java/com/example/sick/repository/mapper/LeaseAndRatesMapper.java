package com.example.sick.repository.mapper;

import com.example.sick.api.model.response.LeaseAndRatesDAOResponse;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LeaseAndRatesMapper implements RowMapper<LeaseAndRatesDAOResponse> {


    @Override
    public LeaseAndRatesDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new LeaseAndRatesDAOResponse(
                resultSet.getLong("id"),
                resultSet.getString("make"),
                resultSet.getString("model"),
                resultSet.getString("modelVariant"),
                resultSet.getString("year"),
                resultSet.getString("fuelType"),
                resultSet.getDouble("enginePower"),
                resultSet.getDouble("engineSize"),
                resultSet.getString("url"),
                resultSet.getString("offer"),
                resultSet.getBoolean("terms"),
                resultSet.getBoolean("confirmation"),
                resultSet.getBigDecimal("carValue"),
                resultSet.getInt("period"),
                resultSet.getBigDecimal("downPayment"),
                resultSet.getInt("residualValuePercentage"),
                resultSet.getBoolean("isEcoFriendly"),
                resultSet.getBigDecimal("monthlyPayment")
        );
    }
}
