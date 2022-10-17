package com.techelevator.model;

import java.util.Date;

public class ForumPost {

    // Instance Variables

    private int postId;
    private String postTitle;
    private String postBody;
    private String imagePath;
    private int upVotes;
    private int downVotes;
    private int userId;
    private int forumId;
    private Date createDate;

    // Constructors

    public ForumPost() {
    }

    public ForumPost(int postId, String postTitle, String postBody, String imagePath, int upVotes, int downVotes, int userId, int forumId, Date createDate) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.imagePath = imagePath;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.userId = userId;
        this.forumId = forumId;
        this.createDate = createDate;
    }

    // Methods

    // Getters and Setters

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
