package org.hcom.config.security.authorize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserStatus;
import org.hcom.models.user.support.UserRepository;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        log.debug("login success");
        User user = userRepository.findByUsername(request.getParameter("username")).orElseThrow(NoSuchUserFoundException::new);
        if(user.getUserStatus().equals(UserStatus.INACTIVE)) {
            log.debug("INACTIVE USER LOGIN");
            httpSession.removeAttribute("user");
            httpSession.setAttribute("inactiveUser", request.getParameter("username"));
            throw new AccountExpiredException("INACTIVE");
        } else if(user.getUserStatus().equals(UserStatus.BLOCKED)){
            log.debug("BLOCKED USER LOGIN");
            httpSession.removeAttribute("user");
            throw new DisabledException("BLOCKED");
        } else if(user.getUserStatus().equals(UserStatus.ACTIVE)) {
            if (user.getFailCount() >= 5) {
                log.debug("LOCKED USER LOGIN");
                httpSession.removeAttribute("user");
                throw new LockedException("LOCKED");
            } else {
                user.setFailCount(0);
                userRepository.save(user);
                resultRedirectStrategy(request, response, authentication);
            }
        }
    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest!=null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            String defaultUrl = "/";
            redirectStrategy.sendRedirect(request, response, defaultUrl);
        }
        clearAuthenticationAttributes(request);
    }
}

