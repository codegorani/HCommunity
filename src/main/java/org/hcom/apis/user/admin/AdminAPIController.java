package org.hcom.apis.user.admin;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.common.ResponseResult;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.admin.request.AdminModifyRequestDTO;
import org.hcom.models.user.dtos.request.UserGradeRequestDTO;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
public class AdminAPIController {

    private final AdminService adminService;

    @PutMapping("/api/v1/admin/user/{username}")
    public ResponseResult<UserInAppResponseDTO> adminUserModifyControl(@RequestBody AdminModifyRequestDTO requestDTO,
                                                                       @LoginUser SessionUser sessionUser,
                                                                       @PathVariable("username") String username) {
        ResponseResult<UserInAppResponseDTO> result = ResponseResult.responseResult(adminService.userInAppModifyService(requestDTO, username, sessionUser));
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        return result;
    }

    @DeleteMapping("/api/v1/admin/user/{username}")
    public Long adminUserDeleteControl(@PathVariable("username") String username, @LoginUser SessionUser sessionUser) {
        return adminService.userDeleteService(username, sessionUser);
    }

    @PutMapping("/api/v1/admin/user/{username}/point")
    public ResponseResult<UserInAppResponseDTO> adminUserPointChangeControl(@PathVariable("username") String username,
                                                                            @LoginUser SessionUser sessionUser, int point) {
        ResponseResult<UserInAppResponseDTO> result = ResponseResult.responseResult(adminService.userPointChangeService(username, sessionUser, point));
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        return result;
    }

    @PutMapping(value = "/api/v1/admin/user/{username}/status")
    public Long adminUserStatusChangeControl(@PathVariable("username") String username,
                                             @LoginUser SessionUser sessionUser, UserGradeRequestDTO requestDTO) {
        return adminService.userStatusChangeService(username, sessionUser, requestDTO);
    }
}
