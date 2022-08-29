package org.hcom.controllers.error;

import org.hcom.exception.article.NoSuchArticleFoundException;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.exception.user.NotLoginUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorControllerAdvice {

    @ExceptionHandler(NoPermissionException.class)
    public String handleNoPermissionException(NoPermissionException ex) {
        return "error/no-permission";
    }

    @ExceptionHandler(NotLoginUserException.class)
    public String handleNotLoginUserException(NotLoginUserException ex) {
        return "error/not-login";
    }

    @ExceptionHandler(NoSuchUserFoundException.class)
    public String handleNoSuchUserFoundException(NoSuchUserFoundException ex) {
        return "error/no-such-user";
    }

    @ExceptionHandler(NoSuchArticleFoundException.class)
    public String handleNoSuchArticleFoundException(NoSuchArticleFoundException ex) {
        return "error/no-such-article";
    }
}
