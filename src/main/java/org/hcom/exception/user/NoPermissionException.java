package org.hcom.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoPermissionException extends RuntimeException {

    public NoPermissionException() {
        super("NO PERMISSION");
    }

}
