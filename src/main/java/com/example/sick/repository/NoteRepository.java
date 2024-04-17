package com.example.sick.repository;

import com.example.sick.domain.NoteDAORequest;
import com.example.sick.domain.NoteDAOResponse;
import com.example.sick.repository.mapper.NoteMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NoteRepository implements NoteRepositoryInterface {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NoteRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<NoteDAOResponse> selectNotesById(long applicationId) {
        String query = """
                    SELECT id, application_id, note_text
                    FROM notes
                    WHERE application_id = :applicationId
                    ORDER BY created_at DESC
                    LIMIT 1;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("applicationId", applicationId);

        return namedParameterJdbcTemplate.query(query, params, new NoteMapper())
                .stream()
                .findFirst();
    }

    @Override
    public long createNote(NoteDAORequest note) {
        String query = """
                    INSERT INTO notes (application_id, note_text)
                    VALUES(:applicationId, :noteText)
                    RETURNING id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("applicationId", note.applicationId())
                .addValue("noteText", note.noteText());

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}
