package org.hcom.apis.user;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.models.common.ResponseResult;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.dtos.request.*;
import org.hcom.models.user.dtos.response.UserFindAccountResponseDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.models.user.dtos.response.UserPersonalResponseDTO;
import org.hcom.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class UserAPIController {

    private final UserService userService;
    private final HttpSession httpSession;

    @PostMapping("/signup/username/auth")
    public String usernameAuthenticate(@RequestBody String username) {
        return userService.usernameAuthService(username);
    }

    @PostMapping("/signup/nickname/auth")
    public String nicknameAuthenticate(@RequestBody String nickname) {
        return userService.nicknameAuthService(nickname);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> userSignupAPIControl(@Validated @RequestBody UserSaveRequestDTO requestDTO) {
//        if (bindingResult.hasErrors()) {
//            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
//            // 200 response with 404 status code
//            return ResponseEntity.ok(new ErrorResponse("404", "Validation failure", errors));
//            // or 404 request
//            //  return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
//        }
        return ResponseEntity.ok(userService.userSaveService(requestDTO));
    }

    @GetMapping("/api/v1/user/{username}")
    public ResponseResult<UserInAppResponseDTO> userInAppDataAPIControl(@PathVariable("username") String username,
                                                                        @LoginUser SessionUser sessionUser) {
        ResponseResult<UserInAppResponseDTO> result =
                ResponseResult.responseResult(userService.userInAppResponseService(username, sessionUser));
        if(result == null) {
            throw new NoPermissionException();
        }
        return result;
    }

    @GetMapping("/api/v1/my/{username}")
    public ResponseResult<UserPersonalResponseDTO> userPersonalDataAPIControl(@PathVariable("username") String username,
                                                                              @LoginUser SessionUser sessionUser) {
        ResponseResult<UserPersonalResponseDTO> result =
                ResponseResult.responseResult(userService.userPersonalResponseService(username, sessionUser));
        if(result == null) {
            throw new NoPermissionException();
        }
        return result;
    }

    @PutMapping("/api/v1/my/resetPassword")
    public String userPasswordUpdateControl(@RequestBody UserResetPasswordRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        return userService.userPasswordResetService(requestDTO, sessionUser);
    }

    @PutMapping("/api/v1/my/{username}")
    public ResponseResult<UserPersonalResponseDTO> userPerDataModifyControl(@RequestBody UserModifyRequestDTO requestDTO,
                                         @PathVariable("username") String username, @LoginUser SessionUser sessionUser) {
        ResponseResult<UserPersonalResponseDTO> result =
                ResponseResult.responseResult(userService.userPersonalModifyService(requestDTO, username, sessionUser));
        if(result == null) {
            throw new NoPermissionException();
        }
        return result;
    }

    @DeleteMapping("/api/v1/my/{username}")
    public void userDeleteControl(@PathVariable("username") String username, @LoginUser SessionUser sessionUser) {
        userService.userDeleteService(username, sessionUser);
    }

    @PostMapping("/forgotPassword")
    public UserFindAccountResponseDTO userFindAccountControl(@RequestBody UserFindAccountDTO dto) {
        UserFindAccountResponseDTO responseDTO = userService.userFindAccountService(dto);
        if(responseDTO.getResult().equals("FOUNDED")) {
            httpSession.setAttribute("resetPassword", true);
        }
        return responseDTO;
    }

    @PostMapping("/forgotPassword/reset")
    public String userForgotPasswordResetControl(@RequestBody UserPasswordResetDTO dto) {
        return userService.userForgotPasswordResetService(dto);
    }
}
