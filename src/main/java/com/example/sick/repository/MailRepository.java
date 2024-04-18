package com.example.sick.repository;

import com.example.sick.domain.MailDAORequest;
import com.example.sick.domain.MailDAOResponse;
import com.example.sick.repository.mapper.MailMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MailRepository implements MailRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MailRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<MailDAOResponse> selectMailByApplicationId(long applicationId) {
        String query = """
                    SELECT id, application_id, mail_text
                    FROM mail
                    WHERE application_id = :applicationId
                    ORDER BY created_at DESC
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("applicationId", applicationId);

        return namedParameterJdbcTemplate.query(query, params, new MailMapper());
    }

    @Override
    public void createMail(MailDAORequest mail) {
        String query = """
                    INSERT INTO notes (application_id, mail_text)
                    VALUES(:applicationId, :mailText)
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("applicationId", mail.applicationId())
                .addValue("mailText", mail.mailText());

        namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}
