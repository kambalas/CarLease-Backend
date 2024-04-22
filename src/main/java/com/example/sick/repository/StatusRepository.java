package com.example.sick.repository;

import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.domain.StatusDAORequest;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.repository.mapper.PersonalInformationMapper;
import com.example.sick.repository.mapper.StatusMapper;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StatusRepository implements StatusRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatusRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    @Override
    public void createStatus(long id) {
        String query = """
                INSERT INTO STATUS (id, status, isOpened, updatedAt, createdAt)
                VALUES (:id, :status, false, now(), now())
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", ApplicationStatus.NEW.toString());
        namedParameterJdbcTemplate.update(query, params);

    }

    @Override
    public void updateStatusById(StatusDAORequest status) {
        String query = """
                UPDATE STATUS
                SET status = :status
                WHERE id = :id;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", status.id())
                .addValue("status", status.APPLICATIONSTATUS().toString());
        namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public Optional<StatusDAOResponse> getStatusById(long id){
        String query = """
                SELECT *
                FROM STATUS
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.query(query, params, new StatusMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<StatusDAOResponse> getAllStatus(){
        String query = """
                SELECT *
                FROM STATUS
                """;
        return namedParameterJdbcTemplate.query(query, new StatusMapper());
    }

    @Override
    public List<StatusDAOResponse> getAllStatusByPage(long pageNumber){
        String query = """
            SELECT *
            FROM STATUS
            LIMIT 7 OFFSET :offset
            """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", (pageNumber - 1) * 7);

        return namedParameterJdbcTemplate.query(query, params, new StatusMapper());

    }

    @Override
    public void updateStatusRead(long id) {
        String query = """
                UPDATE STATUS
                SET isOpened = true
                WHERE id = :id;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(query, params);
    }

}
