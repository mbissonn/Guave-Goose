package com.techelevator.dao;

import com.techelevator.model.ForumsModerator;
import com.techelevator.model.UserStatus;

import java.util.List;

public interface UserStatusDao {

    List<UserStatus> getAllUserForumDataByForum(int forumId);

    UserStatus getUserForumDataByUserIdAndForumId(int forumId, int userId);

    List<ForumsModerator> getAllForumsWhereIsModerator(int userId);
}


