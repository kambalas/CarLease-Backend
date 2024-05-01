package com.example.sick.repository;

import com.example.sick.domain.PersonalInformationDAORequest;
import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.repository.mapper.PersonalInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonalInformationRepository implements PersonalInformationRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PersonalInformationRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public List<PersonalInformationDAOResponse> getAllPersonalInformation() {
        String query = """
                SELECT *
                FROM PERSONAL_INFORMATION
                """;
        return namedParameterJdbcTemplate.query(query, new PersonalInformationMapper());
    }

    @Override
    public List<PersonalInformationDAOResponse> getAllPersonalInformationByPage(long pageNumber){
        String query = """
            SELECT *
            FROM PERSONAL_INFORMATION
            LIMIT 7 OFFSET :offset
            """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", (pageNumber - 1) * 7);

        return namedParameterJdbcTemplate.query(query, params, new PersonalInformationMapper());

    }

    public Optional<PersonalInformationDAOResponse> getPersonalInformationById(long pid) {
        String query = """
                SELECT *
                FROM PERSONAL_INFORMATION
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", pid);

        return namedParameterJdbcTemplate.query(query, params, new PersonalInformationMapper())
                .stream()
                .findFirst();

    }


    public long createPersonalInformation(PersonalInformationDAORequest personalInformationDAORequest) {
        String query = """
                INSERT INTO PERSONAL_INFORMATION (firstName, lastName, email, phoneNumber, pid, dateOfBirth, maritalStatus, numberOfChildren, citizenship, monthlyIncome, languagePref)
                VALUES (:firstName, :lastName, :email, :phoneNumber, :pid, :dateOfBirth, :maritalStatus, :numberOfChildren, :citizenship, :monthlyIncome, :languagePref)
                RETURNING id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", personalInformationDAORequest.firstName())
                .addValue("lastName", personalInformationDAORequest.lastName())
                .addValue("email", personalInformationDAORequest.email())
                .addValue("phoneNumber", personalInformationDAORequest.phoneNumber())
                .addValue("pid", personalInformationDAORequest.pid())
                .addValue("dateOfBirth", personalInformationDAORequest.dateOfBirth())
                .addValue("maritalStatus", personalInformationDAORequest.maritalStatus())
                .addValue("numberOfChildren", personalInformationDAORequest.numberOfChildren())
                .addValue("citizenship", personalInformationDAORequest.citizenship())
                .addValue("monthlyIncome", personalInformationDAORequest.monthlyIncome())
                .addValue("languagePref", personalInformationDAORequest.languagePref());

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}
