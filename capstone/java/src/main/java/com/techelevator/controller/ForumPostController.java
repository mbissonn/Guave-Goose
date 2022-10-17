package com.techelevator.controller;

import com.techelevator.dao.ForumPostDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Exceptions.UserNotAMemberException;
import com.techelevator.model.Exceptions.UserNotAuthorizedException;
import com.techelevator.model.ForumPost;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class ForumPostController {

    private final ForumPostDao forumPostDao;
    private final UserDao userDao;

    // RETRIEVE Methods

    public ForumPostController(ForumPostDao forumPostDao, UserDao userDao) {
        this.forumPostDao = forumPostDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/post")
    public List<ForumPost> getAllPosts() {
        return forumPostDao.findAllPosts();
    }

    @GetMapping(path = "/user/{userId}/post")
    public List<ForumPost> getPostsByUser(@PathVariable int userId) {
        return forumPostDao.findPostsByUser(userId);
    }

    @GetMapping(path = "/forum/{forumId}/post")
    public List<ForumPost> getPostsByForum(@PathVariable int forumId) {
        return forumPostDao.findPostsByForum(forumId);
    }

    @GetMapping(path = "/post/search")
    public List<ForumPost> getPostsByKeyWords(@RequestParam String keywords) {
        return forumPostDao.findPostsByKeywords(keywords);
    }

    @GetMapping(path = "/post/{postId}")
    public ForumPost getPostById(@PathVariable int postId) {
        return forumPostDao.getPostById(postId);
    }



    // CREATE, UPDATE, DELETE Methods

    @PostMapping(path = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ForumPost createPost(@Valid @RequestBody ForumPost forumPost, Principal principal) {
        try {
            //forumPost = getPostById(forumPost.getPostId());
            //System.out.println(forumPost.getPostId());
            int userId = userDao.findIdByUsername(principal.getName());
//            forumPostDao.getForumRoleId(forumPost.getForumId(), userId);
            return forumPostDao.create(forumPost, userId);
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping(path = "/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ForumPost updatePost(@Valid @RequestBody ForumPost forumPost, @PathVariable int postId, Principal principal) {
        int userIdPrincipal = userDao.findIdByUsername(principal.getName());
        if (principal.getName().equals(userDao.getUserById(forumPostDao.getPostById(postId).getUserId()).getUsername()) || forumPostDao.getForumRoleId(forumPost.getForumId(), userIdPrincipal) == 2) {
            return forumPostDao.update(forumPost, postId);
        } else{
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(path = "/forum/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    public void deletePost(@PathVariable int postId, Principal principal) {
        ForumPost forumPost = forumPostDao.getPostById(postId);
        if (principal.getName().equals(userDao.getUserById(forumPost.getUserId()).getUsername()) || forumPostDao.getForumRoleId(forumPost.getForumId(), userDao.findIdByUsername(principal.getName())) == 2) {
            forumPostDao.delete(postId);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    // Additional Methods

    @PutMapping(path = "/post/{postId}/vote")
    @PreAuthorize("isAuthenticated()")
    public void vote(@PathVariable int postId, @RequestParam int value,Principal principal){
        forumPostDao.vote(postId, userDao.findIdByUsername(principal.getName()),value);
    }
}
