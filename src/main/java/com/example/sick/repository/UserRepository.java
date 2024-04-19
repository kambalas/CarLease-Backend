package com.example.sick.repository;


import com.example.sick.domain.AuthenticationDAORequest;
import com.example.sick.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public AuthenticationDAORequest findByUsername(String username) {
    String query = """
                SELECT username, password
                FROM "user"
                WHERE username = :username
            """;
    return namedParameterJdbcTemplate.queryForObject(
            query,
            new MapSqlParameterSource("username", username),
            new UserRowMapper()
    );
  }

  public void save(AuthenticationDAORequest user) {
    String sql = """
                
            INSERT INTO "user" (username, password) VALUES (:username, :password)
                """;

    SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("username", user.getUsername())
            .addValue("password", user.getPassword());

    namedParameterJdbcTemplate.update(sql,
            parameters);


  }
}