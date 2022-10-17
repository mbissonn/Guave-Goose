package com.techelevator.controller;

import javax.validation.Valid;

import com.techelevator.model.Answers;
import com.techelevator.model.Exceptions.UserNotActiveException;
import com.techelevator.model.Exceptions.UserNotAuthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.dao.UserDao;
import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;
import com.techelevator.model.User;
import com.techelevator.model.Exceptions.UserAlreadyExistsException;
import com.techelevator.security.jwt.JWTFilter;
import com.techelevator.security.jwt.TokenProvider;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDto) throws UserNotActiveException {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        User user = userDao.findByUsername(loginDto.getUsername());

        HttpHeaders httpHeaders = new HttpHeaders();
        if (userDao.getActiveStatus(userDao.findIdByUsername(loginDto.getUsername()))) {
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new LoginResponse(jwt, user), httpHeaders, HttpStatus.OK);
        } else {
            throw new UserNotActiveException();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDTO newUser) {
        try {
            User user = userDao.findByUsername(newUser.getUsername());
            throw new UserAlreadyExistsException();
        } catch (UsernameNotFoundException e) {
            userDao.create(newUser.getUsername(), newUser.getPassword(), newUser.getRole());
        }
    }

    @GetMapping(value = "/user")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Boolean> getAllUsernames() {
        return userDao.getAllUsernamesAndActiveStatuses();
    }

    @GetMapping(path = "/user/{userId}/answers")
    public Answers getAnswers(@PathVariable int userId) {
        return userDao.getAnswers(userId);
    }

    @PutMapping(value = "/user/answer")
    @PreAuthorize("isAuthenticated()")
    public String answerQuestion(@RequestParam int question, @RequestBody String answer, Principal principal) {
        return userDao.answerQuestion(answer, userDao.findIdByUsername(principal.getName()), question);
    }

    @PutMapping("value = /user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public boolean setActiveStatus(@PathVariable int userId, Principal principal) {
        if (principal.getName().equals("admin")) {
            return userDao.setActiveStatus(userId);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class LoginResponse {

        private String token;
        private User user;

        LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        @JsonProperty("token")
        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }

        @JsonProperty("user")
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}

