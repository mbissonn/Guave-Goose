package com.techelevator.dao;
import com.techelevator.model.ForumsModerator;
import com.techelevator.model.UserStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
//public class JdbcUserStatusDao implements UserStatusDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public JdbcUserStatusDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public List<UserStatus> getAllUserForumDataByForum(int forumId) {
//        List<UserStatus> allUserStatuses = new ArrayList<>();
//        String sql = "SELECT username, forum_role_id, is_favorited, user_forum.answer_1, user_forum.answer_2, user_forum.answer_3 FROM user_forum JOIN users USING (user_id) WHERE forum_id = ?";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumId);
//        while (results.next()) {
//            UserStatus userStatusResult = mapRowToUserStatus(results);
//            allUserStatuses.add(userStatusResult);
//        }
//        return allUserStatuses;
//    }
//
//    @Override
//    public UserStatus getUserForumDataByUserIdAndForumId(int forumId, int userId) {
//        UserStatus userStatus = null;
//        String sql = "SELECT username, forum_role_id, is_favorited, user_forum.answer_1, user_forum.answer_2, user_forum.answer_3 FROM user_forum JOIN users USING (user_id) WHERE user_id = ? AND forum_id = ?";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, forumId);
//        if (results.next()) {
//            userStatus = mapRowToUserStatus(results);
//        }
//        return userStatus;
//    }
//
//    @Override
//    public List<ForumsModerator> getAllForumsWhereIsModerator(int userId) {
//        List<ForumsModerator> forumsWhereIsModerator = new ArrayList<>();
//        String sql = "SELECT forum_title, forum_id FROM user_forum JOIN forums USING (forum_id) WHERE user_id = ? AND forum_role_id = 2;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
//        while (results.next()) {
//            forumsWhereIsModerator.add(mapRowToForumsModerator(results));
//        }
//        return forumsWhereIsModerator;
//    }
//
//
//
//    public UserStatus mapRowToUserStatus(SqlRowSet rowSet) {
//        UserStatus userStatus = new UserStatus();
//        userStatus.setUsername(rowSet.getString("username"));
//        userStatus.setForumRoleId(rowSet.getInt("forum_role_id"));
//        userStatus.setFavorited(rowSet.getBoolean("is_favorited"));
//        userStatus.setAnswer1(rowSet.getString("answer_1"));
//        userStatus.setAnswer2(rowSet.getString("answer_2"));
//        userStatus.setAnswer3(rowSet.getString("answer_3"));
//        return userStatus;
//    }
//
//    public ForumsModerator mapRowToForumsModerator(SqlRowSet rowset) {
//        ForumsModerator forumsModerator = new ForumsModerator();
//        forumsModerator.setForumTitle(rowset.getString("forum_title"));
//        forumsModerator.setForumId(rowset.getInt("forum_id"));
//        return forumsModerator;
//    }
//
//
//
//}
