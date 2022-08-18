package org.hcom.services.test;

import lombok.RequiredArgsConstructor;
import org.hcom.models.user.User;
import org.hcom.models.user.support.UserMapper;
import org.hcom.models.user.support.UserRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TestService {

    private final UserMapper userMapper;
    private final UserRepositorySupport userRepositorySupport;

    @Transactional
    public User mybatisTest(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Transactional
    public User querydslTest(String username) {
        return userRepositorySupport.findByUsername(username);
    }
}
