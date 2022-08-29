package org.hcom.exception.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchArticleFoundException extends RuntimeException {

    public NoSuchArticleFoundException() {
        super("ARTICLE NOT FOUND");
    }
}
