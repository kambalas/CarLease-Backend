package com.example.sick.repository;

import com.example.sick.domain.CalculatorDAOResponse;
import com.example.sick.repository.mapper.CalculatorRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CalculatorRepository implements CalculatorRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CalculatorRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public CalculatorDAOResponse selectAllInterestRate() {
        String query = """
                    SELECT regular, eco
                    FROM interest_rate
                """;
        return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(), new CalculatorRowMapper());
    }
}
