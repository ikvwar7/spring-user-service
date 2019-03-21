package spring.user.service.dao;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import spring.user.service.domain.User;
import spring.user.service.exceptions.UserNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserDaoJdbc implements UserDao {

    private final String SAVE_USER = "INSERT INTO USER (firstName, lastName, birthday, email, password) VALUES " +
            "(:firstName , :lastName, :birthday, :email, :password)";

    private final String DELETE_BY_ID = "DELETE FROM USER WHERE id = :id";

    private final String FIND_BY_EMAIL = "SELECT * FROM USER WHERE email = :email";

    private final String FIND_BY_ID = "SELECT * FROM USER WHERE id = :id";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(User user) {
        String password = DigestUtils.md5Hex(user.getPassword());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", user.getFirstName());
        params.addValue("lastName", user.getLastName());
        params.addValue("birthday", user.getBirthday());
        params.addValue("email", user.getEmail());
        params.addValue("password", password);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            findByEmail(user.getEmail());
        } catch (UserNotFoundException e) {
            jdbcTemplate.update(SAVE_USER, params, keyHolder);
            user.setId(keyHolder.getKey().longValue());
            user.setPassword(password);
            return;
        }

        throw new RuntimeException("User with email {" +
                user.getEmail() + "} already exists");
    }

    @Override
    public void delete(Long id) {
        try {
            findById(id);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);

            jdbcTemplate.update(DELETE_BY_ID, params);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("There is no user with id = {" + id + "}");
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("email", email);

            return jdbcTemplate.queryForObject(FIND_BY_EMAIL, params, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("There is no user with email = {" + email + "}");
        }
    }

    private User findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.queryForObject(FIND_BY_ID, params, new UserRowMapper());
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            User user = new User(id, firstName, lastName, birthday, email, password);
            return user;
        }
    }
}
