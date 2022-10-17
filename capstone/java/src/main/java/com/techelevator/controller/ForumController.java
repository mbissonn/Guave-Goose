package com.techelevator.controller;

import com.techelevator.dao.ForumDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class ForumController {
    private final ForumDao forumDao;
    private final UserDao userDao;

    public ForumController(ForumDao forumDao, UserDao userDao) {
        this.forumDao = forumDao;
        this.userDao = userDao;
    }

    // RETRIEVE Methods

    @GetMapping(value = "/forum")
    public List<Forum> getAllForums() {
        return forumDao.findAllForums();
    }

    @GetMapping(value = "/forum/{forumId}")
    public Forum getForumById(@PathVariable int forumId) {
        return forumDao.getForumById(forumId);
    }

    @GetMapping(value = "/user/{userId}/forum")
    public List<Forum> getForumsByUserId(@PathVariable int userId) {
        return forumDao.findForumsByUser(userId);
    }

    @GetMapping(value = "/forum/post/{postId}")
    public Forum getForumByPost(@PathVariable int postId){
        return forumDao.getForumByPostId(postId);
    }

    @GetMapping(value = "/forum/search")
    public List<Forum> getForumFromKeywords(@RequestParam String keywords) {
        return forumDao.findForumsByKeywords(keywords);
    }

    @GetMapping(value = "/user/member")
    @PreAuthorize("isAuthenticated()")
    public List<Forum> getMemberForums(Principal principal){
        return forumDao.findMemberForums(userDao.findIdByUsername(principal.getName()));
    }

    @GetMapping(value = "/user/favorite")
    @PreAuthorize("isAuthenticated()")
    public List<Forum> getFavoriteForums(Principal principal){
        return forumDao.findFavoriteForums(userDao.findIdByUsername(principal.getName()));
    }

    @GetMapping(value = "/forum/{forumId}/user")
    public int isMember(@PathVariable int forumId, Principal principal) {
        return forumDao.moderatorCheck(forumId, userDao.findIdByUsername(principal.getName()));
    }



    // CREATE, UPDATE, and DELETE Methods

    @PostMapping(value = "/forum")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Forum createForum(@RequestBody Forum forum, Principal principal) {
        return forumDao.create(forum, userDao.findIdByUsername(principal.getName()));
    }

    @PutMapping(value = "/forum/{forumId}")
    @PreAuthorize("isAuthenticated()")
    public Forum updateForum(@PathVariable int forumId, @RequestBody Forum forum, Principal principal) {
        if (principal.getName().equals(userDao.getUserById(forumDao.getForumById(forumId).getUserId()).getUsername()) || forumDao.moderatorCheck(forum.getForumId(), userDao.findIdByUsername(principal.getName())) == 2) {
            return forumDao.update(forum, forumId);
        }
        return null;
    }

    @DeleteMapping(value = "/forum/{forumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteForum(@PathVariable int forumId, Principal principal) {
        Forum forum = forumDao.getForumById(forumId);
        if (principal.getName().equals(userDao.getUserById(forum.getUserId()).getUsername()) || forumDao.moderatorCheck(forum.getForumId(), userDao.findIdByUsername(principal.getName())) == 2) {
            forumDao.delete(forumId);
        }
    }

    // Additional Methods

    @PostMapping(value = "user/forum/{forumId}")
    @PreAuthorize("isAuthenticated()")
    public void userJoinForum(@PathVariable int forumId, Principal principal) {
        forumDao.userJoinForum(userDao.findIdByUsername(principal.getName()), forumId);
    }

    @GetMapping(value = "/userstatus/forum/{forumId}")
    @PreAuthorize("isAuthenticated()")
    public List<UserStatus> getAllUserStatusByForum(@PathVariable int forumId) {
        return forumDao.getAllUserForumDataByForum(forumId);
    }

    @PutMapping(value = "user/forum/{forumId}")
    @PreAuthorize("isAuthenticated()")
    public void requestModeratorStatus(@PathVariable int forumId, Principal principal) {
        forumDao.requestModeratorStatus(forumId, userDao.findIdByUsername(principal.getName()));
    }

    @PutMapping(value = "user/forum/{forum_id}/moderate")
    @PreAuthorize("isAuthenticated()")
    public void grantModeratorStatus(@RequestParam int pending_user_id, @PathVariable int forum_id, Principal principal) {
        forumDao.grantModeratorStatus(forum_id, pending_user_id, userDao.findIdByUsername(principal.getName()));
    }

    @PutMapping(value = "user/forum/{forum_id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public void setFavorite(@PathVariable int forum_id, Principal principal) {
        forumDao.setFavorite(userDao.findIdByUsername(principal.getName()), forum_id);
    }

    @PutMapping(value = "user/forum/{forumId}/question/{questionId}")
    @PreAuthorize("isAuthenticated()")
    public String answerQuestion(@PathVariable int userId, @PathVariable int forumId, @PathVariable int questionId, @RequestParam String answer, Principal principal) {
        if (userId == userDao.findIdByUsername(principal.getName())) {
            return forumDao.getAnswer(forumId, userDao.findIdByUsername(principal.getName()), questionId) != null ? forumDao.getAnswer(forumId, userId, questionId) : forumDao.answerQuestion(forumId, userId, questionId, answer);
        }
        return null;
    }

    @GetMapping(value = "/user/{userId}/forum/{forumId}/answer")
    @PreAuthorize("isAuthenticated()")
    public Answers getAnswers(@PathVariable int forumId, @PathVariable int userId) {
        return forumDao.getAnswers(forumId, userId);
    }

}
