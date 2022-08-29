package org.hcom.controllers.user;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.hcom.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        doInit();
        String referer = (String) request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        if(referer != null && !fineUrlList.contains(referer)) {
            model.addAttribute("is401", true);
        }

        return "user/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("isError", true);
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
}
