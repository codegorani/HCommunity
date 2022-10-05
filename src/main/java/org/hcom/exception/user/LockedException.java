package org.hcom.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class LockedException extends RuntimeException {

    public LockedException() {
        super("ACCOUNT LOCKED");
    }
}
