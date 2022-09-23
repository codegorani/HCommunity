package org.hcom.exception.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeAlreadyExistException extends RuntimeException {
    public LikeAlreadyExistException() {
        super("Already like exist");
    }
}
