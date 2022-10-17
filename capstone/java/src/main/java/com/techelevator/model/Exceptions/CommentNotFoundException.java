package com.techelevator.model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "Comment Not Found.")
public class CommentNotFoundException extends RuntimeException {

}
