package com.example.sick.repository;

import com.example.sick.domain.AcceptedApplicationDAOResponse;
import com.example.sick.domain.ApplicationDailyCountDAOResponse;
import com.example.sick.domain.ApplicationMonthlyCountDAOResponse;
import com.example.sick.domain.HighRiskDAOResponse;
import com.example.sick.domain.StatusCountDAOResponse;
import com.example.sick.repository.mapper.AcceptedApplicationMapper;
import com.example.sick.repository.mapper.ApplicationMonthlyCountMapper;
import com.example.sick.repository.mapper.ApplicationDailyMapper;
import com.example.sick.repository.mapper.HighRiskMapper;
import com.example.sick.repository.mapper.StatusCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticRepository implements StatisticRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatisticRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public StatusCountDAOResponse countStatus() {
        String query = """
                        SELECT
                        COUNT(CASE WHEN STATUS.status = 'NEW' AND EXTRACT(YEAR FROM STATUS.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) THEN 1 END) AS newCount,
                        COUNT(CASE WHEN STATUS.status = 'ACCEPTED' AND EXTRACT(YEAR FROM STATUS.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) THEN 1 END) AS acceptedCount,
                        COUNT(CASE WHEN STATUS.status = 'REJECTED' AND EXTRACT(YEAR FROM STATUS.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) THEN 1 END) AS rejectedCount,
                        COUNT(CASE WHEN STATUS.status = 'PENDING' AND EXTRACT(YEAR FROM STATUS.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) THEN 1 END) AS pendingCount
                        FROM STATUS;
                """;
        return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(), new StatusCountMapper());
    }

    @Override
    public List<ApplicationDailyCountDAOResponse> countApplicationsCurrentMonthDaily() {
        String query = """
                    SELECT
                        COALESCE(COUNT(STATUS.id), 0) AS applicationCount,
                        date_series.day
                    FROM (
                        SELECT generate_series(date_trunc('month', CURRENT_DATE),
                                               date_trunc('month', CURRENT_DATE) + interval '1 month' - interval '1 day',
                                               interval '1 day') AS day
                    ) AS date_series
                    LEFT JOIN STATUS
                        ON DATE(STATUS.createdAt) = date_series.day
                        AND EXTRACT(MONTH FROM STATUS.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)
                        AND EXTRACT(YEAR FROM STATUS.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)
                    GROUP BY date_series.day
                    ORDER BY date_series.day;
                """;
        return namedParameterJdbcTemplate.query(query, new ApplicationDailyMapper());
    }

    @Override
    public HighRiskDAOResponse countHighRiskApplications() {
        String query = """
                      SELECT
                          COUNT(CASE WHEN isHighRisk = TRUE
                                     AND date_trunc('month', createdAt) = date_trunc('month', CURRENT_DATE)
                                     THEN 1 ELSE NULL END) AS currentMonthHighRiskCount,
                      
                          COUNT(CASE WHEN isHighRisk = TRUE
                                     AND date_trunc('month', createdAt) = date_trunc('month', CURRENT_DATE) - interval '1 month'
                                     THEN 1 ELSE NULL END) AS previousMonthHighRiskCount
                      FROM STATUS
                      WHERE createdAt >= date_trunc('year', CURRENT_DATE) - interval '1 month'
                            AND createdAt < date_trunc('year', CURRENT_DATE) + interval '1 year';
                """;
        return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(), new HighRiskMapper());
    }

    @Override
    public AcceptedApplicationDAOResponse countAcceptedApplicationsTotalSum() {
        String query = """
                SELECT
                    EXTRACT(YEAR FROM date_trunc('year', S.createdAt)) AS leaseYear,
                    SUM(CASE
                            WHEN EXTRACT(YEAR FROM date_trunc('year', S.createdAt)) = EXTRACT(YEAR FROM CURRENT_DATE)
                            THEN L.monthlyPayment * L.period
                            ELSE 0
                        END) AS thisYearTotalPayments,
                    SUM(CASE
                            WHEN EXTRACT(YEAR FROM date_trunc('year', S.createdAt)) = EXTRACT(YEAR FROM CURRENT_DATE) - 1
                            THEN L.monthlyPayment * L.period
                            ELSE 0
                        END) AS lastYearTotalPayments
                FROM LEASE L
                JOIN STATUS S ON L.id = S.id
                WHERE S.status = 'ACCEPTED'
                GROUP BY leaseYear
                ORDER BY leaseYear DESC;
                """;
        return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(), new AcceptedApplicationMapper());
    }

    @Override
    public ApplicationMonthlyCountDAOResponse getApplicationMonthlyCount() {
        String query = """
                SELECT
                    COUNT(CASE WHEN EXTRACT(MONTH FROM STATUS.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) THEN 1 END) AS thisMonthCount,
                    COUNT(CASE WHEN EXTRACT(MONTH FROM STATUS.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) - 1 THEN 1 END) AS previousMonthCount
                FROM STATUS;
                """;
        return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(), new ApplicationMonthlyCountMapper());
    }
}
