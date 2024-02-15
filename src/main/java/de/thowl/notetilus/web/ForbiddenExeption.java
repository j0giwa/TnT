package de.thowl.notetilus.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@StandardException
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access Denied")
class ForbiddenException extends RuntimeException {

}
