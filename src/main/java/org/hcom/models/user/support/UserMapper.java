package org.hcom.models.user.support;

import org.apache.ibatis.annotations.Mapper;
import org.hcom.models.user.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserByUsername(String username);
    List<User> findAll();
}
