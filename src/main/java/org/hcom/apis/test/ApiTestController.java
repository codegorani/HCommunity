package org.hcom.apis.test;

import lombok.RequiredArgsConstructor;
import org.hcom.models.common.ResponseResult;
import org.hcom.models.user.dtos.response.UserInAppResponseDTO;
import org.hcom.services.test.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ApiTestController {

    private final TestService testService;

    @PostMapping("/test1")
    public Map<String,String> test1() {
        Map<String, String> result = new HashMap<>();
        result.put("isSuccess", "true");
        return result;
    }

    @PostMapping("/test2")
    public Map<String,String> test2() {
        Map<String, String> result = new HashMap<>();
        result.put("isSuccess", "false");
        return result;
    }

    @GetMapping("/test/mybatis")
    public ResponseResult<UserInAppResponseDTO> mybatisTest() {
        return ResponseResult.responseResult(new UserInAppResponseDTO(testService.mybatisTest("hjhearts")));
    }

    @GetMapping("/test/querydsl")
    public ResponseResult<UserInAppResponseDTO> queryDslTest() {
        return ResponseResult.responseResult(new UserInAppResponseDTO(testService.querydslTest("hjhearts")));
    }

}
