package com.example.sick.repository;

import com.example.sick.domain.LeaseAndRatesDAORequest;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.repository.mapper.LeaseAndRatesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class LeaseAndRatesRepository implements LeaseAndRatesRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LeaseAndRatesRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    @Override
    public List<LeaseAndRatesDAOResponse> getAllLeaseAndRates() {
        String query = """
                SELECT *
                FROM lease
                """;
        return namedParameterJdbcTemplate.query(query, new LeaseAndRatesMapper());
    }

    @Override
    public List<LeaseAndRatesDAOResponse> getAllLeaseAndRatesByPage(long pageNumber){
        String query = """
            SELECT *
            FROM LEASE
            LIMIT 7 OFFSET :offset
            """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", (pageNumber - 1) * 7);

        return namedParameterJdbcTemplate.query(query, params, new LeaseAndRatesMapper());

    }

    @Override
    public Optional<LeaseAndRatesDAOResponse> getLeaseAndRateById(long pid) {
        String query = """
                SELECT *
                FROM lease
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", pid);

        return namedParameterJdbcTemplate.query(query,params, new LeaseAndRatesMapper())
                .stream()
                .findFirst();
    }

    @Override
    public void createLeaseAndRate(LeaseAndRatesDAORequest leaseAndRatesDAOResponse, long pid) {
        String query = """
                INSERT INTO lease (id, make, model, modelVariant, year, fuelType, enginePower, engineSize, url, offer, terms, confirmation, carValue, period, downPayment, residualValuePercentage, isEcoFriendly, monthlyPayment)
                VALUES (:id, :make, :model, :modelVariant, :year, :fuelType, :enginePower, :engineSize, :url, :offer, :terms, :confirmation, :carValue, :period, :downPayment, :residualValuePercentage, :isEcoFriendly, :monthlyPayment)
                """;
        LobHandler lobHandler = new DefaultLobHandler();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", pid)
                .addValue("make", leaseAndRatesDAOResponse.make())
                .addValue("model", leaseAndRatesDAOResponse.model())
                .addValue("modelVariant", leaseAndRatesDAOResponse.modelVariant())
                .addValue("year", leaseAndRatesDAOResponse.year())
                .addValue("fuelType", leaseAndRatesDAOResponse.fuelType())
                .addValue("enginePower", leaseAndRatesDAOResponse.enginePower())
                .addValue("engineSize", leaseAndRatesDAOResponse.engineSize())
                .addValue("url", leaseAndRatesDAOResponse.url())
                .addValue("offer", leaseAndRatesDAOResponse.offer())
                .addValue("terms", leaseAndRatesDAOResponse.terms())
                .addValue("confirmation", leaseAndRatesDAOResponse.confirmation())
                .addValue("carValue", leaseAndRatesDAOResponse.carValue())
                .addValue("period", leaseAndRatesDAOResponse.period())
                .addValue("downPayment", leaseAndRatesDAOResponse.downPayment())
                .addValue("residualValuePercentage", leaseAndRatesDAOResponse.residualValuePercentage())
                .addValue("isEcoFriendly", leaseAndRatesDAOResponse.isEcoFriendly())
                .addValue("monthlyPayment", leaseAndRatesDAOResponse.monthlyPayment());

        namedParameterJdbcTemplate.update(query, params);
    }

}

