package com.techelevator.dao;

import com.techelevator.model.Exceptions.UserNotAMemberException;
import com.techelevator.model.PostComment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JdbcPostCommentDao implements PostCommentDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostCommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RETRIEVE METHODS

    @Override
    public List<PostComment> findAllComments() {
        List<PostComment> allComments = new ArrayList<>();
        String sqlGetAllComments = "SELECT * FROM comments";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllComments);
        while (results.next()) {
            PostComment commentResult = mapRowToComment(results);
            allComments.add(commentResult);
        }
        return allComments;
    }

    @Override
    public List<PostComment> findCommentsByUser(int userId) {
        List<PostComment> allCommentsByUser = new ArrayList<>();
        String sqlGetAllCommentsByUser = "SELECT * FROM comments WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCommentsByUser, userId);
        while (results.next()) {
            PostComment commentByUserResult = mapRowToComment(results);
            allCommentsByUser.add(commentByUserResult);
        }
        return allCommentsByUser;
    }

    @Override
    public List<PostComment> findCommentsByPost(int postId) {
        List<PostComment> allCommentsByPost = new ArrayList<>();
        String sqlGetAllCommentsByPost = "SELECT * FROM comments WHERE post_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCommentsByPost, postId);
        while (results.next()) {
            PostComment commentByPostResult = mapRowToComment(results);
            allCommentsByPost.add(commentByPostResult);
        }
        return allCommentsByPost;
    }


    @Override
    public List<PostComment> findCommentsByKeywords(String keywords) {
        List<PostComment> allCommentsByKeywords = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE ";
        String sqlSubstring = "";
        List<String> keywordsList = Arrays.asList(keywords.split(" "));
        keywordsList.replaceAll(keyword -> "%" + keyword + "%");
        for (int i = 0; i < keywords.split(" ").length; i++) {
            sqlSubstring = i == 0 ? "comment_body ILIKE ?" : sqlSubstring + " OR comment_body ILIKE ?";
        }
        sql += sqlSubstring;
        Collections.sort(keywordsList);
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, keywordsList.toArray());
        while (results.next()) {
            allCommentsByKeywords.add(mapRowToComment(results));
        }
        return allCommentsByKeywords;
    }

    @Override
    public PostComment getCommentById(int commentId) {
        PostComment commentById = null;
        String sqlGetCommentById = "SELECT * FROM comments WHERE comment_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCommentById, commentId);
        if (results.next()) {
            commentById = mapRowToComment(results);
        }
        return commentById;

    }

    // CREATE, UPDATE, and DELETE METHODS

    @Override
    public PostComment create(PostComment comment, int userId) {
        String sql = "INSERT INTO comments (user_id, comment_body, post_id, date_created) VALUES (?, ?, ?, CURRENT_DATE) RETURNING comment_id, date_created;";
        comment.setUserId(userId);
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, comment.getUserId(), comment.getCommentBody(), comment.getPostId());
        if (result.next()) {
            comment.setCommentId(result.getInt("comment_id"));
            comment = getCommentById(comment.getCommentId());
        }
        return comment;
    }

    @Override
    public void update(PostComment commentToUpdate, int commentId) {
        String sqlUpdateComment = "UPDATE comments SET comment_body = ? WHERE comment_id = ?";
        jdbcTemplate.update(sqlUpdateComment, commentToUpdate.getCommentBody(), commentId);
    }

    @Override
    public void delete(int commentId) {
        String sqlDeleteComment = "DELETE FROM comments WHERE comment_id = ?";
        jdbcTemplate.update(sqlDeleteComment, commentId);
    }

    // Additional Methods

    @Override
    public int getForumId(int postId) {
        String sql = "SELECT forum_id FROM posts WHERE post_id = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }

    @Override
    public int getForumRoleId(int forumId, int userId) {
        String sql = "Select forum_role_id FROM user_forum WHERE forum_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, forumId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAMemberException();
        }
    }

    // MAP Methods

    private PostComment mapRowToComment(SqlRowSet rowSet) {
        PostComment comment = new PostComment();
        comment.setCommentId(rowSet.getInt("comment_id"));
        comment.setCommentBody(rowSet.getString("comment_body"));
        comment.setUserId(rowSet.getInt("user_id"));
        comment.setPostId(rowSet.getInt("post_id"));
        comment.setCreateDate(rowSet.getDate("date_created")); // Needs changed
        return comment;
    }


}
