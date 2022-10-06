package org.hcom.controllers.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.hcom.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private List<String> fineUrlList;
    private final ArticleService articleService;
    private final UserService userService;
    private final HttpSession httpSession;

    public void doInit() {
        fineUrlList = new ArrayList<>();
        fineUrlList.add("http://localhost:8080/");
        fineUrlList.add("http://localhost:8080/article");
        fineUrlList.add("http://localhost:8080/error");
        fineUrlList.add("http://localhost:8080/welcome");
        fineUrlList.add("http://localhost:8080/login");
        fineUrlList.add("http://localhost:8080/forgotPassword");
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model, @LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            log.debug("already login user <<username>>::" + sessionUser.getUsername());
            return "redirect:/";
        }
        doInit();
        String referer = (String) request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "user/login";
    }

    @PostMapping("/login/error")
    public String loginError(Model model) {
        httpSession.removeAttribute("user");
        model.addAttribute("isError", true);
        return "user/login";
    }

    @GetMapping("/login/locked")
    public String loginLocked(Model model) {
        httpSession.removeAttribute("user");
        model.addAttribute("isLocked", true);
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(@LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            log.debug("login user cannot signup <<username>>::" + sessionUser.getUsername());
            return "redirect:/";
        }
        return "user/signup";
    }

    @GetMapping("/welcome")
    public String welcome(@LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            log.debug("login user cannot see welcome <<username>>::" + sessionUser.getUsername());
            return "redirect:/";
        }
        return "user/welcome";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "user/forgot-password";
    }

    @GetMapping("/forgotPassword/reset/{username}")
    public String forgotPasswordReset(@PathVariable("username") String username, Model model) {
        if(httpSession.getAttribute("resetPassword") != null && httpSession.getAttribute("resetPassword").equals(true)) {
            model.addAttribute("username", username);
            httpSession.removeAttribute("resetPassword");
            return "user/forgot-password-reset";
        } else {
            throw new NoPermissionException();
        }
    }
}
