package org.hcom.controllers.test;

import lombok.RequiredArgsConstructor;
import org.hcom.exception.NoSuchElementFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ErrorTestController {

    @GetMapping("/error/rs")
    public String errorTestByResponseStatus() {
        throw new NoSuchElementFoundException();
    }

}
