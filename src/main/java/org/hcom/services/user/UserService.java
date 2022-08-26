package org.hcom.services.user;

import lombok.RequiredArgsConstructor;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.dtos.request.UserModifyRequestDTO;
import org.hcom.models.user.dtos.request.UserSaveRequestDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.models.user.dtos.response.UserPersonalResponseDTO;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.support.UserRepository;
import org.springframework.http.HttpStatus;
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
        System.out.println("username:" + username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        user.setLastLoginTime(LocalDateTime.now());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().getRole()));
        System.out.println(user.getUserRole().getRole());
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
    public Long userSaveService(UserSaveRequestDTO requestDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        requestDTO.setCryptPassword(passwordEncoder);
        return userRepository.save(requestDTO.toEntity()).getIdx();
    }

    /**
     * Update user's password
     * Request
     * @param username user-id
     * @param password new password
     * @return updated user's db index
     * @throws ResponseStatusException if sessionUser is null
     */
    @Transactional
    public Long userPasswordResetService(String username, String password, SessionUser sessionUser) throws ResponseStatusException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        if((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                (sessionUser.getUserRole().equals(UserRole.ADMIN)) || (sessionUser.getUserRole().equals(UserRole.DEVELOPER))) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.passwordReset(passwordEncoder.encode(password));
            return userRepository.save(user).getIdx();
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
        User user = userRepository.findByUsername(username).orElseThrow(IllegalAccessError::new);

        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
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
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);

        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
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
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);

        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        if ((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            likeRepository.deleteAllByUser(user);
            replyRepository.deleteAllByUser(user);
            userRepository.delete(user);
            httpSession.invalidate();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
    }
}
