package com.techelevator.model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "An Answer Has Already Been Provided.")
public class AnswerAlreadyProvidedException extends RuntimeException {

}
