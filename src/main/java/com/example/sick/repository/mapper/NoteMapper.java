package com.example.sick.repository.mapper;

import com.example.sick.domain.NoteDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteMapper implements RowMapper<NoteDAOResponse> {

    @Override
    public NoteDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new NoteDAOResponse(
                resultSet.getLong("id"),
                resultSet.getLong("application_id"),
                resultSet.getString("note_text")
        );
    }
}

