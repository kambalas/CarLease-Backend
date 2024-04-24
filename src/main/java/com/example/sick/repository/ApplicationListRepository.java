package com.example.sick.repository;

import com.example.sick.repository.mapper.ApplicationListMapper;
import com.example.sick.domain.ApplicationListDAORequest;
import com.example.sick.domain.ApplicationListDAOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationListRepository {

    final int PAGE_SIZE = 15;
    
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ApplicationListRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<ApplicationListDAOResponse> sortAndFilterByStatus(ApplicationListDAORequest applicationListRequest) {

        String query = """
        SELECT PERSONAL_INFORMATION.id, PERSONAL_INFORMATION.firstName, PERSONAL_INFORMATION.lastName,
               STATUS.isOpened, STATUS.status, STATUS.updatedAt
        FROM PERSONAL_INFORMATION
        JOIN STATUS ON PERSONAL_INFORMATION.id = STATUS.id
        WHERE STATUS.status IN (:statuses)
        ORDER BY STATUS.updatedAt DESC
        LIMIT :limit OFFSET :offset
        """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("statuses", applicationListRequest.statuses())
                .addValue("limit", PAGE_SIZE)
                .addValue("offset", (applicationListRequest.page() - 1) * PAGE_SIZE);

          return namedParameterJdbcTemplate.query(query, params, new ApplicationListMapper());
    }

    public List<ApplicationListDAOResponse> sortApplicationsByTimestamp(ApplicationListDAORequest applicationListRequest) {
        String query = """
        SELECT PERSONAL_INFORMATION.id, PERSONAL_INFORMATION.firstName, PERSONAL_INFORMATION.lastName,
               STATUS.isOpened, STATUS.status, STATUS.updatedAt
        FROM PERSONAL_INFORMATION
        JOIN STATUS ON PERSONAL_INFORMATION.id = STATUS.id
        ORDER BY STATUS.updatedAt DESC
        LIMIT :limit OFFSET :offset
        """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("limit", PAGE_SIZE)
                .addValue("offset", (applicationListRequest.page() - 1) * PAGE_SIZE);

        return namedParameterJdbcTemplate.query(query, params, new ApplicationListMapper());
    }

    public List<ApplicationListDAOResponse> sortAndFilterBySearchQuery(ApplicationListDAORequest applicationListRequest) {
        String finalName= applicationListRequest.searchQuery().toLowerCase().trim() + "%";

        String query = """
        SELECT PERSONAL_INFORMATION.id, PERSONAL_INFORMATION.firstName, PERSONAL_INFORMATION.lastName,
               STATUS.isOpened, STATUS.status, STATUS.updatedAt
        FROM PERSONAL_INFORMATION
        JOIN STATUS ON PERSONAL_INFORMATION.id = STATUS.id
        WHERE (CONCAT(LOWER(PERSONAL_INFORMATION.firstName), ' ', LOWER(PERSONAL_INFORMATION.lastName)) LIKE :searchQuery
                    OR CAST(PERSONAL_INFORMATION.id AS TEXT) LIKE :searchQuery)
        ORDER BY STATUS.updatedAt DESC
        LIMIT :limit OFFSET :offset
        """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("searchQuery", finalName)
                .addValue("limit", PAGE_SIZE)
                .addValue("offset", (applicationListRequest.page() - 1) * PAGE_SIZE);

        return namedParameterJdbcTemplate.query(query, params, new ApplicationListMapper());
    }

    public List<ApplicationListDAOResponse> sortAndFilterByStatusAndSearchQuery(ApplicationListDAORequest applicationListRequest) {
        String finalName= applicationListRequest.searchQuery().toLowerCase().trim() + "%";

        String query = """
                SELECT PERSONAL_INFORMATION.id, PERSONAL_INFORMATION.firstName, PERSONAL_INFORMATION.lastName,
                    STATUS.isOpened, STATUS.status, STATUS.updatedAt
                FROM PERSONAL_INFORMATION
                JOIN STATUS ON PERSONAL_INFORMATION.id = STATUS.id
                WHERE STATUS.status IN (:statuses)
                AND (CONCAT(LOWER(PERSONAL_INFORMATION.firstName), ' ', LOWER(PERSONAL_INFORMATION.lastName)) LIKE :searchQuery
                    OR CAST(PERSONAL_INFORMATION.id AS TEXT) LIKE :searchQuery)
                ORDER BY STATUS.updatedAt DESC
                LIMIT :limit OFFSET :offset
                  """;

        System.out.println(applicationListRequest.page());
        System.out.println(applicationListRequest.searchQuery());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("statuses",applicationListRequest.statuses())
                .addValue("searchQuery", finalName)
                .addValue("limit", PAGE_SIZE)
                .addValue("offset", (applicationListRequest.page() - 1) * PAGE_SIZE);

        return namedParameterJdbcTemplate.query(query, params, new ApplicationListMapper());
    }
}
