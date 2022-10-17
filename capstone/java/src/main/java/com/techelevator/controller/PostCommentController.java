package com.techelevator.controller;

import com.techelevator.dao.PostCommentDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Exceptions.UserNotAMemberException;
import com.techelevator.model.Exceptions.UserNotAuthorizedException;
import com.techelevator.model.PostComment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class PostCommentController {

    private final PostCommentDao postCommentDao;
    private final UserDao userDao;

    public PostCommentController(PostCommentDao postCommentDao, UserDao userDao) {
        this.postCommentDao = postCommentDao;
        this.userDao = userDao;
    }

    // RETRIEVE Methods

    @GetMapping(path = "/comment")
    public List<PostComment> getAllComments() {
        return postCommentDao.findAllComments();
    }

    @GetMapping(path = "/comment/{commentId}")
    public PostComment getCommentById(@PathVariable int commentId) {
        return postCommentDao.getCommentById(commentId);
    }


    @GetMapping(path = "/user/{userId}/comment")
    public List<PostComment> findCommentsByUser(@PathVariable int userId) {
        return postCommentDao.findCommentsByUser(userId);
    }

    @GetMapping(path = "/post/{postId}/comment")
    public List<PostComment> findCommentsByPost(@PathVariable int postId) {
        return postCommentDao.findCommentsByPost(postId);
    }

    @GetMapping(path = "/comment/search")
    public List<PostComment> findCommentsByKeywords(@RequestParam String keywords) {
        return postCommentDao.findCommentsByKeywords(keywords);
    }

    @GetMapping(path = "/{commentId}/forum")
    public Integer findForumIdByComment(@PathVariable int commentId) {
        return postCommentDao.getForumId(getCommentById(commentId).getPostId());
    }

    // CREATE, UPDATE, and DELETE Methods

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/comment")
    public PostComment create(@Valid @RequestBody PostComment comment, Principal principal) {
        try {
            int userId = userDao.findIdByUsername(principal.getName());
            postCommentDao.getForumRoleId(postCommentDao.getForumId(comment.getPostId()), userId);
            return postCommentDao.create(comment, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotAMemberException();
        }
    }

    @PutMapping(path = "/comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public void update(@PathVariable int commentId, @Valid @RequestBody PostComment commentToUpdate, Principal principal) {
        PostComment commentToPost = getCommentById(commentId);
        if (principal.getName().equals(userDao.getUserById(commentToPost.getUserId()).getUsername()) || postCommentDao.getForumRoleId(postCommentDao.getForumId(commentToPost.getPostId()), userDao.findIdByUsername(principal.getName())) == 2) {
            postCommentDao.update(commentToUpdate, commentId);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

        @ResponseStatus(HttpStatus.NO_CONTENT)
        @DeleteMapping(path = "/comment/{commentId}")
        @PreAuthorize("isAuthenticated()")
        public void delete(@PathVariable int commentId, Principal principal){
            PostComment comment = postCommentDao.getCommentById(commentId);
            if (principal.getName().equals(userDao.getUserById(comment.getUserId()).getUsername()) || postCommentDao.getForumRoleId(postCommentDao.getForumId(comment.getPostId()), userDao.findIdByUsername(principal.getName())) == 2) {
                postCommentDao.delete(comment.getCommentId());
            } else {
                throw new UserNotAuthorizedException();
            }
        }
    }

