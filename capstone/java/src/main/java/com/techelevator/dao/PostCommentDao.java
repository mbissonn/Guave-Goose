package com.techelevator.dao;

import com.techelevator.model.PostComment;
import java.util.List;

public interface PostCommentDao {

    // RETRIEVE Methods

    List<PostComment> findAllComments();

    List<PostComment> findCommentsByUser(int userId);

    List<PostComment> findCommentsByPost(int postId);

    List<PostComment> findCommentsByKeywords(String keywords);

    PostComment getCommentById(int commentId);

    // CREATE, UPDATE, DELETE Methods

    PostComment create(PostComment comment, int userId);

    void update(PostComment commentToUpdate, int commentId);

    void delete(int commentId);

    // Additional Methods

    int getForumId(int postId);

    int getForumRoleId(int forumId, int userId);
}
