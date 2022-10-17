package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.dao.UserStatusDao;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

//@RestController
//@CrossOrigin
//public class UserStatusController {
//
//    private final UserStatusDao userStatusDao;
//    private final UserDao userDao;
//
//    public UserStatusController(UserStatusDao userStatusDao, UserDao userDao) {
//        this.userStatusDao = userStatusDao;
//        this.userDao = userDao;
//    }
//
//    @GetMapping(value = "/userstatus/forum/{forumId}")
//    public List<UserStatus> getAllUserStatusByForum(@PathVariable int forumId) {
//        return userStatusDao.getAllUserForumDataByForum(forumId);
//    }
//
//    @GetMapping(value = "/userstatus/forum/{forumId}/user/{userId}")
//    public UserStatus getUserStatusByForumAndUser(@PathVariable int forumId, @PathVariable int userId) {
//        return userStatusDao.getUserForumDataByUserIdAndForumId(forumId, userId);
//    }
//
//    @GetMapping(value = "user/moderatorforums")
//    public List<ForumsModerator> getAllForumsWhereIsModerator(Principal principal) {
//        return userStatusDao.getAllForumsWhereIsModerator(userDao.findIdByUsername(principal.getName()));
//    }
//
//    @PutMapping(value = "/makemember/forum/{forumId}/user/{userId}")
//    public void makeUserMemberOfForum(@PathVariable int forumId, @PathVariable int userId) {
//
//    }
//
//}
