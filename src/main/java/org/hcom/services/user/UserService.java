package org.hcom.services.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcom.exception.user.LoginFailureException;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.exception.user.NotLoginUserException;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.dtos.request.*;
import org.hcom.models.user.dtos.response.UserFindAccountResponseDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.models.user.dtos.response.UserPersonalResponseDTO;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;
import org.hcom.models.user.support.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final HttpSession httpSession;

    /**
     * Login process from UserDetailService
     * @param username user-id
     * @return User authority
     * @throws UsernameNotFoundException if user-id not exist
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(LoginFailureException::new);
        if(!(user.getLastLoginTime().getYear() == LocalDateTime.now().getYear()) &&
        !(user.getLastLoginTime().getDayOfYear() == LocalDateTime.now().getDayOfYear())) {
            user.modifyUserPoint(20);
        }
        user.setLastLoginTime(LocalDateTime.now());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().getRole()));
        httpSession.setAttribute("user", new SessionUser(user));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    /**
     * Create Process
     * Request
     * @param requestDTO request data
     * @return saved user's db index
     */
    @Transactional
    public String userSaveService(UserSaveRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail()).orElse(null);
        if (user == null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            requestDTO.setCryptPassword(passwordEncoder);
            userRepository.save(requestDTO.toEntity()).getIdx();
            return "SUCCESS";
        } else {
            return user.getUsername();
        }
    }

    /**
     * Update user's password
     * Request
     * @param requestDTO password data
     * @param sessionUser login-user
     * @return updated user's db index
     * @throws ResponseStatusException if sessionUser is null
     */
    @Transactional
    public String userPasswordResetService(UserResetPasswordRequestDTO requestDTO, SessionUser sessionUser) throws ResponseStatusException {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);

        if((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx()))) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(passwordEncoder.matches(requestDTO.getNewPassword(), user.getPassword())) {
                return "NEW_PASSWORD_IS_CURRENT";
            } else if(passwordEncoder.matches(requestDTO.getCurrentPassword(), user.getPassword())) {
                user.passwordReset(passwordEncoder.encode(requestDTO.getNewPassword()));
                userRepository.save(user);
                return "SUCCESS";
            } else {
                return "CURRENT_PASSWORD_ERROR";
            }
        } else {
            return null;
        }
    }

    /**
     * Update Process
     * Request
     * @param requestDTO user's modify data
     * @param username user-id
     * @return updated user's db index
     */
    public UserPersonalResponseDTO userPersonalModifyService(UserModifyRequestDTO requestDTO, String username, SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        if(!username.equals(sessionUser.getUsername())) {
            throw new NoPermissionException();
        }

        User user = userRepository.findByUsername(username).orElseThrow(NoSuchUserFoundException::new);

        if((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                (sessionUser.getUserRole().equals(UserRole.ADMIN)) || (sessionUser.getUserRole().equals(UserRole.DEVELOPER))) {
            user.modifyByUser(requestDTO);
            userRepository.save(user);
            return new UserPersonalResponseDTO(user);
        } else {
            return null;
        }
    }

    /**
     * In-App data(for all user)
     * Response
     * @param username user-id
     * @param sessionUser login-user
     * @return user's in-app used data
     */
    @Transactional
    public UserInAppResponseDTO userInAppResponseService(String username, SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        if(!username.equals(sessionUser.getUsername())) {
            throw new NoPermissionException();
        }

        User user = userRepository.findByUsername(username).orElseThrow(NoSuchUserFoundException::new);

        if((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                (sessionUser.getUserRole().equals(UserRole.ADMIN)) || (sessionUser.getUserRole().equals(UserRole.DEVELOPER))) {
            return new UserInAppResponseDTO(user);
        } else {
            return null;
        }
    }

    /**
     * Personal data(just for user)
     * Response
     * @param username user-id
     * @param sessionUser login-user
     * @return user's personal data
     */
    @Transactional
    public UserPersonalResponseDTO userPersonalResponseService(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username).orElseThrow(NoSuchUserFoundException::new);

        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        if((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                (sessionUser.getUserRole().equals(UserRole.ADMIN)) || (sessionUser.getUserRole().equals(UserRole.DEVELOPER))) {
            return new UserPersonalResponseDTO(user);
        } else {
            return null;
        }
    }

    /**
     * User Delete
     * Request
     * @param username user-id
     */
    @Transactional
    public void userDeleteService(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username).orElseThrow(NoSuchUserFoundException::new);

        if (sessionUser == null) {
            throw new NotLoginUserException();
        }
        if ((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            likeRepository.deleteAllByUser(user);
            replyRepository.deleteAllByUser(user);
            userRepository.delete(user);
            httpSession.invalidate();
        } else {
            throw new NoPermissionException();
        }
    }

    @Transactional
    public String usernameAuthService(String username) {
        String valid;
        if(userRepository.findByUsername(username).isEmpty()) {
            valid = "VALID";
        } else {
            valid = "INVALID";
        }
        return valid;
    }

    @Transactional
    public String nicknameAuthService(String username) {
        String valid;
        if(userRepository.findByNickname(username).isEmpty()) {
            valid = "VALID";
        } else {
            valid = "INVALID";
        }
        return valid;
    }

    @Transactional
    public UserFindAccountResponseDTO userFindAccountService(UserFindAccountDTO dto) {
        User user = userRepository.findByUsernameAndEmail(dto.getUsername(), dto.getEmail()).orElse(null);
        if(user != null) {
             return UserFindAccountResponseDTO.builder()
                     .username(user.getUsername())
                     .result("FOUNDED")
                     .build();
        } else {
             return UserFindAccountResponseDTO.builder()
                     .username(null)
                     .result("NOT_FOUNDED")
                     .build();
        }
    }

    @Transactional
    public String userForgotPasswordResetService(UserPasswordResetDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(NoSuchUserFoundException::new);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            return "NEW_PASSWORD_IS_CURRENT";
        } else {
            user.passwordReset(passwordEncoder.encode(dto.getNewPassword()));
            user.setFailCount(0);
            userRepository.save(user);
            return "SUCCESS";
        }
    }

    @Transactional
    public String userInactiveClearService(UserInactiveClearRequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername()).orElse(null);
        if(user == null) {
            return "USERNAME_ERROR";
        }

        if(!user.getEmail().equals(requestDTO.getEmail())) {
            return "EMAIL_ERROR";
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            user.setUserStatus(UserStatus.ACTIVE);
            user.setFailCount(0);
            httpSession.removeAttribute("inactive");
            httpSession.removeAttribute("inactiveUser");
            return "SUCCESS";
        } else {
            return "PASSWORD_ERROR";
        }
    }
}
