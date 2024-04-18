package com.example.sick.repository.mapper;

import com.example.sick.domain.MailDAOResponse;
import com.example.sick.domain.NoteDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MailMapper implements RowMapper<MailDAOResponse> {

    @Override
    public MailDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new MailDAOResponse(
                resultSet.getLong("id"),
                resultSet.getLong("application_id"),
                resultSet.getString("mail_text")
        );
    }
}

