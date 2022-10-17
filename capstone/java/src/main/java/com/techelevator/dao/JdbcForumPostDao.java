package com.techelevator.dao;

import com.techelevator.model.Exceptions.UserNotAMemberException;
import com.techelevator.model.ForumPost;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class JdbcForumPostDao implements ForumPostDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcForumPostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RETRIEVE Methods

    @Override
    public List<ForumPost> findAllPosts() {
        List<ForumPost> allPosts = new ArrayList<>();
        String sql = "SELECT * FROM posts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            ForumPost forumPost = mapRowToPost(results);
            allPosts.add(forumPost);
        }
        return allPosts;
    }

    @Override
    public List<ForumPost> findPostsByUser(int userId) {
        List<ForumPost> userPosts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            ForumPost forumPost = mapRowToPost(results);
            userPosts.add(forumPost);
        }
        return userPosts;
    }

    @Override
    public List<ForumPost> findPostsByForum(int forumId) {
        List<ForumPost> forumPosts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE forum_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, forumId);
        while (results.next()) {
            ForumPost forumPost = mapRowToPost(results);
            forumPosts.add(forumPost);
        }
        return forumPosts;
    }

    @Override
    public List<ForumPost> findPostsByKeywords(String keywords) {
        List<ForumPost> allPostsByKeywords = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE ";
        String sqlSubstring = "";
        List<String> keywordsList = Arrays.asList(Stream.concat(Arrays.stream(keywords.split(" ")), Arrays.stream(keywords.split(" "))).toArray(String[]::new));
        keywordsList.replaceAll(keyword -> "%" + keyword + "%");
        for (int i = 0; i < keywords.split(" ").length; i++) {
            sqlSubstring = i == 0 ? "post_title ILIKE ?" : sqlSubstring + " OR post_title ILIKE ?";
            sqlSubstring = i == (keywords.split(" ").length - 1) ? sqlSubstring + " OR post_BODY ILIKE ? ;" : sqlSubstring + " OR post_BODY ILIKE ?";
        }
        sql += sqlSubstring;
        Collections.sort(keywordsList);
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, keywordsList.toArray());
        while (results.next()) {
            allPostsByKeywords.add(mapRowToPost(results));
        }
        return allPostsByKeywords;
    }


    @Override
    public ForumPost getPostById(int postId) {
        ForumPost forumPost = null;
        String sql = "SELECT * FROM posts WHERE post_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, postId);
        if (results.next()) {
            forumPost = mapRowToPost(results);
        }
        return forumPost;
    }

    // These Methods Not Used

//    @Override
//    public ForumPost findPostByTitle(String postTitle) {
//        ForumPost forumPost = null;
//        String sql = "SELECT * FROM posts WHERE post_title ILIKE ?;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + postTitle + "%");
//        if (results.next()) {
//            forumPost = mapRowToPost(results);
//        }
//        return forumPost;
//    }
//
//    @Override
//    public int findPostIdByTitle(String postTitle) {
//        Integer postId = null;
//        String sql = "SELECT post_id FROM posts WHERE post_title = ?;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, postTitle);
//        if (results.next()) {
//            postId = results.getInt("post_id");
//        }
//        return postId;
//    }

    // CREATE, UPDATE, DELETE Methods

    @Override
    public ForumPost create(ForumPost postToCreate, int userId) {
        String sql = "INSERT INTO posts (post_title, post_body, up_votes, down_votes, user_id, forum_id, date_posted) VALUES (?,?,0,0,?,?, CURRENT_DATE ) RETURNING post_id, date_posted;";
        postToCreate.setUserId(userId);
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, postToCreate.getPostTitle(), postToCreate.getPostBody(), userId, postToCreate.getForumId());
        if (result.next()) {
            postToCreate.setPostId(result.getInt("post_id"));
            postToCreate = getPostById(postToCreate.getPostId());
        }
        return postToCreate;
    }

    @Override
    public ForumPost update(ForumPost postToUpdate, int postId) {
        String sql = "UPDATE posts SET post_title = ?, post_body = ?, image_source = ? WHERE post_id = ?;";
        jdbcTemplate.update(sql, postToUpdate.getPostTitle(), postToUpdate.getPostBody(), postToUpdate.getImagePath(), postId);
        postToUpdate = getPostById(postId);
        return postToUpdate;
    }

    @Override
    public boolean delete(int postId) {
        String sql = "DELETE FROM posts WHERE post_id = ?;";
        jdbcTemplate.update(sql, postId);
        return true;
    }

    // Additional Methods

    @Override
    public int getForumRoleId(int forumId, int userId) {
        String sql = "Select forum_role_id FROM user_forum WHERE forum_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, forumId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAMemberException();
        }
    }

    @Override
    public void vote(int postId, int userId, int voteValue) {
        String sql = "SELECT * FROM user_post WHERE user_id = ? AND post_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, postId);
        if (results.next()) {
            sql = "UPDATE user_post SET vote_value = ? WHERE user_id = ? AND post_id = ?;";
            jdbcTemplate.update(sql, voteValue, userId, postId);
        } else {
            try {
                getForumRoleId(getPostById(postId).getForumId(), userId);
                sql = "INSERT INTO user_post (user_id, post_id, vote_value) VALUES (?, ?, ?);";
                jdbcTemplate.update(sql, userId, postId, voteValue);
            } catch (EmptyResultDataAccessException ex) {
                throw new UserNotAMemberException();
            }
        }
        updatePostVotes(postId);
    }

    private void updatePostVotes(int postId) {
        String sql = "SELECT vote_value FROM user_post WHERE post_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, postId);
        int upVotes = 0;
        int downVotes = 0;
        while (results.next()) {
            if (results.getInt("vote_value") > 0) {
                upVotes += results.getInt("vote_value");
            } else {
                downVotes += results.getInt("vote_value");
            }
        }
        sql = "UPDATE posts SET up_votes = ? WHERE post_id = ?;";
        jdbcTemplate.update(sql, upVotes, postId);
        sql = "UPDATE posts SET down_votes = ? WHERE post_id = ?;";
        jdbcTemplate.update(sql, -downVotes, postId);
    }

    // These Methods Not Used

//    @Override
//    public boolean postCompare(ForumPost javaPost, ForumPost sqlPost) {
//        if (
//                javaPost.getPostTitle().equals(sqlPost.getPostTitle()) &&
//                        javaPost.getPostBody().equals(sqlPost.getPostBody()) &&
//                        ((sqlPost.getImagePath() == null && javaPost.getImagePath() == null) || javaPost.getImagePath().equals(sqlPost.getImagePath()))
//        ) {
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public int getVoteStatus(int postId, int userId) {
//        Integer voteStatusVal = null;
//        String sql = "SELECT vote_status FROM user_post WHERE user_id = ? AND post_id = ?;";
//        try {
//            voteStatusVal = jdbcTemplate.queryForObject(sql, Integer.class, userId, postId);
//        } catch (Exception e) {
//            String sqlHasNotVoted = "INSERT INTO user_post (user_id, post_id, vote_status) VALUES (?,?,?);";
//            jdbcTemplate.update(sqlHasNotVoted, userId, postId, 0);
//            return 0;
//        }
//        return voteStatusVal;
//    }

    // MAP Methods

    private ForumPost mapRowToPost(SqlRowSet rowSet) {
        ForumPost post = new ForumPost();
        post.setPostId(rowSet.getInt("post_id"));
        post.setPostTitle(rowSet.getString("post_title"));
        post.setPostBody(rowSet.getString("post_body"));
        post.setImagePath(rowSet.getString("image_source"));
        post.setUpVotes(rowSet.getInt("up_votes"));
        post.setDownVotes(rowSet.getInt("down_votes"));
        post.setUserId(rowSet.getInt("user_id"));
        post.setForumId(rowSet.getInt("forum_id"));
        post.setCreateDate(rowSet.getDate("date_posted")); // Needs changed
        return post;
    }
}
