package com.techelevator.dao;

import com.techelevator.model.Answers;
import com.techelevator.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    // RETRIEVE Methods

    List<User> findAll();

    User getUserById(int userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    // CREATE Method

    boolean create(String username, String password, String role);

    // Additional Methods

    Answers getAnswers(int userId);

    public String answerQuestion(String answer, int userId, int questionId);

    boolean getActiveStatus(int userId);

    public boolean setActiveStatus(int userId);

    public Map<String, Boolean> getAllUsernamesAndActiveStatuses();

    }
