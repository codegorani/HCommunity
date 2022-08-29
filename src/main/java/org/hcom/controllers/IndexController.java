package org.hcom.controllers;

import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.user.dtos.SessionUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            model.addAttribute("sessionUser", sessionUser);
        }
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "/user/ajax_test";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/error/{errorCode}")
    public String errorHandle(@PathVariable("errorCode") int error) {
        throw new ResponseStatusException(HttpStatus.valueOf(error));
    }
}
