package org.hcom.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotLoginUserException extends RuntimeException {

    public NotLoginUserException() {
        super("NOT LOGIN USER");
    }
}
