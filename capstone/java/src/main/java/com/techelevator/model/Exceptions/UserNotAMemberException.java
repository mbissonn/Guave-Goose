package com.techelevator.model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "User Not a Member of this Forum.")
public class UserNotAMemberException extends RuntimeException {

}
