package com.techelevator.model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "User Has Exceeded Forum Creation Limit (3).")
public class ExceededForumCreationLimitException extends RuntimeException {

}
