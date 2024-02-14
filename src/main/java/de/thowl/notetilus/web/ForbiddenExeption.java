package de.thowl.notetilus.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access Denied")
class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
