package org.hcom.services.admin;

import lombok.RequiredArgsConstructor;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.dtos.admin.request.AdminModifyRequestDTO;
import org.hcom.models.user.dtos.request.UserGradeRequestDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.support.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public UserInAppResponseDTO userInAppModifyService(AdminModifyRequestDTO requestDTO, String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username).orElseThrow(IllegalAccessError::new);
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NOT LOGIN");
        }
        if(sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            user.modifyByAdmin(requestDTO);
            userRepository.save(user);
            return new UserInAppResponseDTO(user);
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
    public Long userDeleteService(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        if ((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            userRepository.delete(user);
            return user.getIdx();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
    }

    @Transactional
    public UserInAppResponseDTO userPointChangeService(String username, SessionUser sessionUser, int point) {
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        if ((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            user.setUserPoint(point);
            userRepository.save(user);
            return new UserInAppResponseDTO(user);
        } else {
            return null;
        }
    }

    @Transactional
    public Long userStatusChangeService(String username, SessionUser sessionUser, UserGradeRequestDTO requestDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        if ((sessionUser.getUsername().equals(user.getUsername()) && sessionUser.getIdx().equals(user.getIdx())) ||
                sessionUser.getUserRole().equals(UserRole.ADMIN) || sessionUser.getUserRole().equals(UserRole.DEVELOPER)) {
            user.setUserGrade(requestDTO.getUserGrade());
            return userRepository.save(user).getIdx();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
    }
}
