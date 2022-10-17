package com.techelevator.dao;

import com.techelevator.model.ForumPost;

import java.util.List;

public interface ForumPostDao {

    // RETRIEVE Methods

    List<ForumPost> findAllPosts();

    List<ForumPost> findPostsByUser(int userId);

    List<ForumPost> findPostsByForum(int forumId);

    List<ForumPost> findPostsByKeywords(String keywords);

    ForumPost getPostById(int postId);

        // These Methods Not Used

//    ForumPost findPostByTitle(String postTitle);
//
//    int findPostIdByTitle(String postTitle);

    // CREATE, UPDATE, DELETE Methods

    ForumPost create(ForumPost forumPost, int userId);

    ForumPost update(ForumPost postToUpdate, int postId);

    boolean delete(int postId);

    // Additional Methods

    int getForumRoleId(int forumId, int userId);

    void vote(int postId, int userId, int voteValue);

        // These Method Not Used

//    boolean postCompare(ForumPost javaPost, ForumPost sqlPost);

//    public int getVoteStatus(int postId, int userId);


}
