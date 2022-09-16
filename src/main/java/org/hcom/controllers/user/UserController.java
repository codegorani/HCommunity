package org.hcom.controllers.user;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.hcom.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private List<String> fineUrlList;
    private final ArticleService articleService;
    private final UserService userService;

    public void doInit() {
        fineUrlList = new ArrayList<>();
        fineUrlList.add("http://localhost:8080/");
        fineUrlList.add("http://localhost:8080/article");
        fineUrlList.add("http://localhost:8080/error");
        fineUrlList.add("http://localhost:8080/welcome");
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model, boolean isRememberMe, @LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            return "redirect:/";
        }
        doInit();
        String referer = (String) request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        if(referer != null && !fineUrlList.contains(referer)) {
            model.addAttribute("is401", true);
        }

        return "user/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model, @LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            return "redirect:/";
        }
        model.addAttribute("isError", true);
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(@LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            return "redirect:/";
        }
        return "user/signup";
    }

    @GetMapping("/welcome")
    public String welcome(@LoginUser SessionUser sessionUser) {
        if(sessionUser != null) {
            return "redirect:/";
        }
        return "user/welcome";
    }
}
