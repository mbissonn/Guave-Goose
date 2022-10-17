package com.techelevator.model;

public class ForumsModerator {
    private String forumTitle;
    private int forumId;

    public ForumsModerator(String forumTitle, int forumId) {
        this.forumTitle = forumTitle;
        this.forumId = forumId;
    }

    public ForumsModerator(){}

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
}
