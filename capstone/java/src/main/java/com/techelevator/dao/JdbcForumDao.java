package com.techelevator.dao;

import com.techelevator.model.*;
import com.techelevator.model.Exceptions.ExceededForumCreationLimitException;
import com.techelevator.model.Exceptions.ForumNotFoundException;
import com.techelevator.model.Exceptions.UserNotAMemberException;
import com.techelevator.model.Exceptions.UserNotAuthorizedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class JdbcForumDao implements ForumDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcForumDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RETRIEVE Methods

    @Override
    public List<Forum> findAllForums() {
        List<Forum> allForums = new ArrayList<>();
        String sqlGetAllForums = "SELECT * FROM forums";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllForums);
        while (results.next()) {
            Forum forumResult = mapRowToForum(results);
            allForums.add(forumResult);
        }
        return allForums;
    }

    @Override
    public List<Forum> findForumsByUser(int userId) {
        List<Forum> forums = new ArrayList<>();
        String sql = "SELECT * FROM forums WHERE original_poster_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        try{
        while (results.next()) {
            Forum forum = mapRowToForum(results);
            forums.add(forum);
        }} catch (Exception e){
            return forums;
        }
//        if (forums.size() == 0) {
            ///throw new ForumNotFoundException()
        //}
        return forums;
    }

    @Override
    public List<Forum> findForumsByKeywords(String keywords) {
        List<Forum> allForumsByKeywords = new ArrayList<>();
        String sql = "SELECT * FROM forums WHERE ";
        String sqlSubstring = "";
        List<String> keywordsList = Arrays.asList(keywords.split(" "));
        keywordsList.replaceAll(keyword -> "%" + keyword + "%");
        for (int i = 0; i < keywords.split(" ").length; i++) {
            sqlSubstring = i == 0 ? " forum_title ILIKE ?" : i != keywordsList.size() ? sqlSubstring + " OR forum_title ILIKE ?" : sqlSubstring + " OR forum_title ILIKE ?;";
        }
        sql += sqlSubstring;
        Collections.sort(keywordsList);
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, keywordsList.toArray());
        while (results.next()) {
            allForumsByKeywords.add(mapRowToForum(results));
        }
        return allForumsByKeywords;
    }

    @Override
    public List<Forum> findMemberForums(int userId) {
        List<Forum> memberForums = new ArrayList<>();
        String sql = "SELECT * FROM forums JOIN user_forum USING(forum_id) WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            memberForums.add(mapRowToForum(results));
        }
        return memberForums;
    }

    @Override
    public Forum getForumByPostId(int postId) throws ForumNotFoundException {
        Forum forum = null;
        String sql = "SELECT * FROM forums JOIN posts USING (forum_id) WHERE post_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, postId);
        if (results.next()) {
            forum = mapRowToForum(results);
        }
        return forum;
    }

    @Override
    public List<Forum> findFavoriteForums(int userId) {
        List<Forum> favoriteForums = new ArrayList<>();
        String sql = "SELECT * FROM forums JOIN user_forum USING(forum_id) WHERE user_id = ? AND is_favorited = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, true);
        while (results.next()) {
            favoriteForums.add(mapRowToForum(results));
        }
        return favoriteForums;
    }

    @Override
    public Forum getForumById(int forumId) {
        Forum forum = null;
        String sql = "SELECT * FROM forums where forum_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumId);
        if (results.next()) {
            forum = mapRowToForum(results);
        }
        return forum;
    }

    // These Methods Not Used

//    @Override
//    public Forum findForumByTitle(String forumTitle) {
//        Forum forum = null;
//        String sql = "SELECT * FROM forums WHERE forum_title LIKE '%'|| ? || '%'";
//
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumTitle);
//        if (results.next()) {
//            forum = mapRowToForum(results);
//        }
//        return forum;
//    }
//
//    @Override
//    public int findForumIdByTitle(String forumTitle) {
//        String sql = "SELECT forum_id FROM forums WHERE forum_title ILIKE '%' || ? '%'";
//        Integer id = null;
//        id = jdbcTemplate.queryForObject(sql, Integer.class, forumTitle);
//        return Objects.requireNonNullElse(id, -1);
//    }

    // CREATE, UPDATE, and DELETE Methods

    @Override
    public Forum create(Forum forum, int userId) {
        if (findForumsByUser(userId).size() < 3) {
            String sql = "INSERT INTO forums (forum_title, original_poster_user_id, date_created, question_1, question_2, question_3, forum_description, forum_image) VALUES (?,?,CURRENT_DATE,?,?,?,?,?) returning forum_id";
            forum.setUserId(userId);
            forum.setForumId(jdbcTemplate.queryForObject(sql, Integer.class, forum.getForumTitle(), userId, forum.getQuestion1(), forum.getQuestion2(), forum.getQuestion3(), forum.getDescription(), forum.getImageSource()));
            forum = getForumById(forum.getForumId());
            sql = "INSERT INTO user_forum(user_id, forum_id, forum_role_id) values (?,?,?);";
            jdbcTemplate.update(sql, userId, forum.getForumId(), 2);
            return forum;
        } else {
            throw new ExceededForumCreationLimitException();
        }
    }

    @Override
    public Forum update(Forum forumToUpdate, int forumId) {
        String sqlUpdateForum = "UPDATE forums SET forum_title = ?, question_1 = ?, question_2 = ? , question_3 = ?, forum_description = ?, forum_image = ? WHERE forum_id = ?";
        jdbcTemplate.update(sqlUpdateForum, forumToUpdate.getForumTitle(), forumToUpdate.getQuestion1(), forumToUpdate.getQuestion2(), forumToUpdate.getQuestion3(), forumToUpdate.getDescription(), forumToUpdate.getImageSource(), forumId);
        forumToUpdate = getForumById(forumId);
        return forumToUpdate;
    }

    @Override
    public void delete(int forumId) throws ForumNotFoundException {
        String sql = "DELETE FROM user_forum where forum_id = ?;";
        jdbcTemplate.update(sql, forumId);
        sql = "DELETE FROM forums WHERE forum_id = ? ;";
        jdbcTemplate.update(sql, forumId);
    }

    // Additional Methods

    @Override
    public void userJoinForum(int userId, int forumId) {
        String sql = "INSERT INTO user_forum(user_id,forum_id,forum_role_id) values (?,?,?)";
        jdbcTemplate.update(sql, userId, forumId, 0);
    }

    @Override
    public List<UserStatus> getAllUserForumDataByForum(int forumId) {
        List<UserStatus> allUserStatuses = new ArrayList<>();
        String sql = "SELECT username, user_id, forum_role_id, is_favorited, user_forum.answer_1, user_forum.answer_2, user_forum.answer_3 FROM user_forum JOIN users USING (user_id) WHERE forum_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumId);
        while (results.next()) {
            UserStatus userStatusResult = mapRowToUserStatus(results);
            allUserStatuses.add(userStatusResult);
        }
        return allUserStatuses;
    }



    @Override
    public void requestModeratorStatus(int forumId, int userId) {
        setForumRoleId(userId, forumId, 1);
    }

    @Override
    public void grantModeratorStatus(int forumId, int userPendingId, int userGrantingId) {
        String sql = "select forum_role_id from user_forum where user_id = ? and forum_id = ?;";
        int grantingUserForumRoleId = -1;
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userGrantingId, forumId);
        if (result.next()) {
            grantingUserForumRoleId = result.getInt("forum_role_id");
        }
        if (grantingUserForumRoleId == 2 && userGrantingId != userPendingId) {
            sql = "UPDATE user_forum set forum_role_id = ? where user_id = ? AND forum_id = ?";
            jdbcTemplate.update(sql, 2, userPendingId, forumId);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    public int moderatorCheck(int forumId, int userId) {
        String sql = "Select forum_role_id FROM user_forum WHERE forum_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, forumId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAuthorizedException();
        }
    }

    private void setForumRoleId(int userId, int forumId, int forumRoleId) {
        String sql = "UPDATE user_forum SET forum_role_id = ? WHERE user_id = ? AND forum_id = ? RETURNING forum_role_id;";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, forumRoleId, userId, forumId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAMemberException();
        }
    }

    @Override
    public String answerQuestion(int forumId, int userId, int questionId, String answer) {
        String sql = "UPDATE user_forum SET ";
        String sqlSubstring = "";
        switch (questionId) {
            case 1:
                sqlSubstring = "answer_1 = ? WHERE user_id = ? AND forum_id = ?;";
                break;
            case 2:
                sqlSubstring = "answer_2 = ? WHERE user_id = ? AND forum_id = ?;";
                break;
            case 3:
                sqlSubstring = "answer_3 = ? WHERE user_id = ? AND forum_id = ?;";
                break;
        }
        sql += sqlSubstring;
        jdbcTemplate.update(sql, answer, userId, forumId);
        return answer;
    }

    @Override
    public String getAnswer(int forumId, int userId, int questionId) {
        String sql = "SELECT ";
        String sqlSubstring = "";
        String answer = null;
        switch (questionId) {
            case 1:
                sqlSubstring = "answer_1 FROM user_forum WHERE user_id = ? AND forum_id = ?;";
                break;
            case 2:
                sqlSubstring = "answer_2 FROM user_forum WHERE user_id = ? AND forum_id = ?;";
                break;
            case 3:
                sqlSubstring = "answer_3 FROM user_forum WHERE user_id = ? AND forum_id = ?;";
                break;
        }
        sql += sqlSubstring;

        answer = jdbcTemplate.queryForObject(sql, String.class, userId, forumId);
        return answer;
    }

    @Override
    public Answers getAnswers(int forumId, int userId) {
        String sql = "select answer_1, answer_2, answer_3 from user_forum where forum_id = ? and user_id = ?";
        Answers answersToReturn = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumId, userId);
        if (results.next()) {
            answersToReturn = mapRowToAnswers(results);
        }
        return answersToReturn;
    }

    @Override
    public void setFavorite(int userId, int forumId) throws UserNotAMemberException {
        try {
            String sql = "Select is_favorited from user_forum where forum_id = ? and user_id = ?";
            boolean isFavorite = jdbcTemplate.queryForObject(sql, Boolean.class, forumId, userId);
            isFavorite = !isFavorite;
            sql = "UPDATE user_forum set is_favorited = ? where user_id = ? and forum_id = ?";
            jdbcTemplate.update(sql, isFavorite, userId, forumId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAMemberException();
        }
    }

    // Currently, not needed, given that users must be members of forums to favorite them. Will need reworked to allow users to favorite forums of which they are not members

//    private void insertIntoFavorites(int userId, int forumId) {
//        String sql = "INSERT INTO user_forum(user_id,forum_id,is_favorited) values (?,?,true)";
//        jdbcTemplate.update(sql, userId, forumId);
//    }

    // MAP Methods

    private Forum mapRowToForum(SqlRowSet rowSet) {
        Forum forum = new Forum();
        forum.setForumId(rowSet.getInt("forum_id"));
        forum.setForumTitle(rowSet.getString("forum_title"));
        forum.setUserId(rowSet.getInt("original_poster_user_id"));
        forum.setCreateDate(rowSet.getDate("date_created"));
        forum.setDescription(rowSet.getString("forum_description"));
        forum.setImageSource(rowSet.getString("forum_image"));
        forum.setQuestion1(rowSet.getString("question_1"));
        forum.setQuestion2(rowSet.getString("question_2"));
        forum.setQuestion3(rowSet.getString("question_3"));
        return forum;
    }

    private Answers mapRowToAnswers(SqlRowSet rowSet) {
        Answers answers = new Answers();
        answers.setAnswerOne(rowSet.getString("answer_1"));
        answers.setAnswerTwo(rowSet.getString("answer_2"));
        answers.setAnswerThree(rowSet.getString("answer_3"));
        return answers;
    }

    public UserStatus mapRowToUserStatus(SqlRowSet rowSet) {
        UserStatus userStatus = new UserStatus();
        userStatus.setUsername(rowSet.getString("username"));
        userStatus.setUserId(rowSet.getInt("user_id"));
        userStatus.setForumRoleId(rowSet.getInt("forum_role_id"));
        userStatus.setFavorited(rowSet.getBoolean("is_favorited"));
        userStatus.setAnswer1(rowSet.getString("answer_1"));
        userStatus.setAnswer2(rowSet.getString("answer_2"));
        userStatus.setAnswer3(rowSet.getString("answer_3"));
        return userStatus;
    }
}
