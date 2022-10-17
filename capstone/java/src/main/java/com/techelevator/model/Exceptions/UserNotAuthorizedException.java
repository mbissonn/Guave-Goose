package com.techelevator.model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "User Not Authorized For This Action.")
public class UserNotAuthorizedException extends RuntimeException {

}
