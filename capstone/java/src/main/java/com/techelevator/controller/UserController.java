package com.techelevator.controller;
import com.techelevator.model.User;
import io.jsonwebtoken.Jwt;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.techelevator.dao.UserDao;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userdao) {
        this.userDao = userdao;
    }

    @GetMapping(path = "/user/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userDao.getUserById(userId);
    }
}
