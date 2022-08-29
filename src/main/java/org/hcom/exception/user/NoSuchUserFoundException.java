package org.hcom.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchUserFoundException extends RuntimeException {

    public NoSuchUserFoundException() {
        super("USER NOT FOUND");
    }
}
