package com.techelevator.dao;

import java.util.*;

import com.techelevator.model.Answers;
import com.techelevator.model.Exceptions.AnswerAlreadyProvidedException;
import com.techelevator.model.Exceptions.ForumNotFoundException;
import com.techelevator.model.Exceptions.UserNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.techelevator.model.User;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RETRIEVE Methods

    @Override
    public int findIdByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        int userId;
        try {
            userId = jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }

        return userId;
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToUser(results);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        for (User user : this.findAll()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    // CREATE Method

    @Override
    public boolean create(String username, String password, String role) {
        String insertUserSql = "insert into users (username,password_hash,role) values (?,?,?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        String ssRole = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase();

        return jdbcTemplate.update(insertUserSql, username, password_hash, ssRole) == 1;
    }

    // Additional Methods

    @Override
    public Answers getAnswers(int userId) throws UserNotFoundException, ForumNotFoundException {
        String sql = "select answer_1, answer_2, answer_3 from users where user_id = ?";
        Answers answersToReturn = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            answersToReturn = mapRowToAnswers(results);
        }
        return answersToReturn;
    }

    @Override
    public String answerQuestion(String answer, int userId, int questionId) {
        try {
            String sql = "SELECT answer_" + questionId + " FROM users WHERE user_id = ?";
            if (jdbcTemplate.queryForObject(sql, String.class, userId) == null) {
                sql = "UPDATE users SET answer_" + questionId + " = ? WHERE user_id = ? RETURNING answer_" + questionId + ";";
                return jdbcTemplate.queryForObject(sql, String.class, answer, userId);
            } else {
                throw new AnswerAlreadyProvidedException();
            }
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean getActiveStatus(int userId) {
        try {
            String sql = "SELECT active FROM users WHERE user_id = ?";
            Boolean active;
            active = jdbcTemplate.queryForObject(sql, Boolean.class, userId);
            return active;
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean setActiveStatus(int user_id) {
        try {
            String sql = "SELECT active FROM users WHERE user_id = ?;";
            Boolean activeStatus = jdbcTemplate.queryForObject(sql, Boolean.class, user_id);
            activeStatus = !activeStatus;
            sql = "UPDATE users SET active = ? RETURNING active;";
            return jdbcTemplate.queryForObject(sql, Boolean.class, activeStatus);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Map<String, Boolean> getAllUsernamesAndActiveStatuses() {
        Map<String, Boolean> usernameStatusMap = new LinkedHashMap<>();
        List<User> users = findAll();
        for (User user : users) {
            if (!user.getUsername().equals("admin")) {
                usernameStatusMap.put(user.getUsername(), user.isActive());
            }
        }
        return usernameStatusMap;
    }

    // MAP Methods

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setAuthorities(Objects.requireNonNull(rs.getString("role")));
        user.setActivated(true);
        user.setActive(rs.getBoolean("active"));
        user.setAnswer_1(rs.getString("answer_1"));
        user.setAnswer_2(rs.getString("answer_2"));
        user.setAnswer_3(rs.getString("answer_3"));
        return user;
    }

    private Answers mapRowToAnswers(SqlRowSet rowSet) {
        Answers answers = new Answers();
        answers.setAnswerOne(rowSet.getString("answer_1"));
        answers.setAnswerTwo(rowSet.getString("answer_2"));
        answers.setAnswerThree(rowSet.getString("answer_3"));
        return answers;
    }
}
