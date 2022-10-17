package com.techelevator.dao;

import com.techelevator.model.*;
import com.techelevator.model.Exceptions.ForumNotFoundException;
import com.techelevator.model.Exceptions.UserNotFoundException;

import java.util.List;


public interface ForumDao {

    // RETRIEVE Methods

    List<Forum> findAllForums();

    List<Forum> findForumsByUser(int userId) throws ForumNotFoundException, UserNotFoundException;

    List<Forum> findForumsByKeywords(String keywords);

    List<Forum> findMemberForums(int userId);

    List<Forum> findFavoriteForums(int userId);


    Forum getForumById(int forumId);

        // These Methods Not Used

//    Forum findForumByTitle(String forumTitle);
//
//    int findForumIdByTitle(String forumTitle);

    // CREATE, UPDATE, and DELETE Methods

    Forum create(Forum forumToCreate, int userId);

    Forum update(Forum forumToUpdate, int forumId);

    void delete(int forumId);

    // Other Methods

    void userJoinForum(int userId, int forumId);

    List<UserStatus> getAllUserForumDataByForum(int forumId);

    void requestModeratorStatus(int forumId, int userId);

    Forum getForumByPostId(int postId) throws ForumNotFoundException;

    void grantModeratorStatus(int forumId, int userPendingId, int userGrantingId);

    int moderatorCheck(int forumId, int userId);

    String answerQuestion(int forumId, int userId, int questionId, String answer);

    String getAnswer(int forumId, int userId, int questionId);

    Answers getAnswers(int forumId, int userId);

    void setFavorite(int userId, int forumId);



}
