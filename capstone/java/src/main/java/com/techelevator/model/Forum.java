package com.techelevator.model;

import java.util.Date;

public class Forum {

    // Instance Variables

    private int forumId;
    private String forumTitle;
    private int userId;
    private Date createDate;
    private String question1;
    private String question2;
    private String question3;
    private String imageSource;
    private String description;

    public String getImageSource() {
        return imageSource;
    }
    
    // Constructors

    public Forum() {}

    public Forum(int forumId, String forumTitle, boolean activeStatus, int userId, Date createDate, String question1, String question2, String question3,String imageSource, String description) {
        this.forumId = forumId;
        this.forumTitle = forumTitle;
        this.userId = userId;
        this.createDate = createDate;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.imageSource = imageSource;
        this.description = description;
    }

    // Methods

    // Getters and Setters

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
