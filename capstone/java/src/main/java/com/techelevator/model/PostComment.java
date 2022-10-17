package com.techelevator.model;

import java.util.Date;

public class PostComment {

    // Instance Variables

    private int commentId;
    private String commentBody;
    private int userId;
    private int postId;
    private Date createDate;

    // Constructors

    public PostComment() {
    }

    public PostComment(int commentId, String commentBody, int userId, int postId, Date createDate) {
        this.commentId = commentId;
        this.commentBody = commentBody;
        this.userId = userId;
        this.postId = postId;
        this.createDate = createDate;
    }

    // Methods

    // Getters and Setters


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
