package org.hcom.apis.user;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.common.ResponseResult;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.dtos.request.UserModifyRequestDTO;
import org.hcom.models.user.dtos.request.UserSaveRequestDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.models.user.dtos.response.UserPersonalResponseDTO;
import org.hcom.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserAPIController {

    private final UserService userService;

    @PostMapping("/signup")
    public Long userSignupAPIControl(@RequestBody UserSaveRequestDTO requestDTO) {
        return userService.userSaveService(requestDTO);
    }

    @GetMapping("/api/v1/user/{username}")
    public ResponseResult<UserInAppResponseDTO> userInAppDataAPIControl(@PathVariable("username") String username,
                                                                        @LoginUser SessionUser sessionUser) {
        ResponseResult<UserInAppResponseDTO> result =
                ResponseResult.responseResult(userService.userInAppResponseService(username, sessionUser));
        if(result == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        return result;
    }

    @GetMapping("/api/v1/my/{username}")
    public ResponseResult<UserPersonalResponseDTO> userPersonalDataAPIControl(@PathVariable("username") String username,
                                                                              @LoginUser SessionUser sessionUser) {
        ResponseResult<UserPersonalResponseDTO> result =
                ResponseResult.responseResult(userService.userPersonalResponseService(username, sessionUser));
        if(result == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        return result;
    }

    @PutMapping("/api/v1/my/{username}/password")
    public Long userPasswordUpdateControl(@PathVariable("username") String username, String password,
                                          @LoginUser SessionUser sessionUser) {
        return userService.userPasswordResetService(username, password, sessionUser);
    }

    @PutMapping("/api/v1/my/{username}")
    public ResponseResult<UserPersonalResponseDTO> userPerDataModifyControl(@RequestBody UserModifyRequestDTO requestDTO,
                                         @PathVariable("username") String username, @LoginUser SessionUser sessionUser) {
        ResponseResult<UserPersonalResponseDTO> result =
                ResponseResult.responseResult(userService.userPersonalModifyService(requestDTO, username, sessionUser));
        if(result == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        return result;
    }

    @DeleteMapping("/api/v1/my/{username}")
    public void userDeleteControl(@PathVariable("username") String username, @LoginUser SessionUser sessionUser) {
        userService.userDeleteService(username, sessionUser);
    }

}
