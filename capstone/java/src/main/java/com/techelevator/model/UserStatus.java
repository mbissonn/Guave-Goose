package com.techelevator.model;

public class UserStatus {
    private String username;
    private int userId;
    private int forumRoleId;
    private boolean isFavorited;
    private String answer1;
    private String answer2;
    private String answer3;

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserStatus(String username, int userId, int forumRoleId, boolean isFavorited, String answer1,
                      String answer2, String answer3) {
        this.username = username;
        this.userId = userId;
        this.forumRoleId = forumRoleId;
        this.isFavorited = isFavorited;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    public UserStatus(){};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getForumRoleId() {
        return forumRoleId;
    }

    public void setForumRoleId(int forumRoleId) {
        this.forumRoleId = forumRoleId;
    }
}
