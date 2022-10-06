package org.hcom.config.security.authorize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcom.models.user.User;
import org.hcom.models.user.support.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.debug("onAuthenticationFailure start");
        if(exception instanceof BadCredentialsException) {
            log.debug("login fail");
            User user = userRepository.findByUsername(request.getParameter("username")).orElse(null);
            if (user == null) {
                throw new IllegalArgumentException("USER NOT FOUND");
            }
            user.increaseFailCount();
            userRepository.save(user);

            if(user.getFailCount() >= 5) {
                log.debug("Locked!");
                request.setAttribute("isLocked", true);
            } else {
                request.setAttribute("isError", true);
            }

        } else if(exception instanceof InternalAuthenticationServiceException) {
            request.setAttribute("isError", true);
        } else if(exception instanceof LockedException) {
            request.setAttribute("isLocked", true);
        } else if(exception instanceof DisabledException) {
            request.setAttribute("isBlocked", true);
        } else if(exception instanceof AccountExpiredException) {
            httpSession.setAttribute("inactive", true);
            request.setAttribute("isInactive", true);
        }

        request.getRequestDispatcher("/login/error").forward(request, response);
    }
}
