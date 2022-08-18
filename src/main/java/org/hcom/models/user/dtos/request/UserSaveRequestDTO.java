package org.hcom.models.user.dtos.request;

import lombok.*;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequestDTO {

    private String username;
    private String password;
    private String nickname;
    private String lastName;
    private String firstName;
    private String birth;
    private String email;
    private String phoneNum;
    private String telNum;
    private String address1;
    private String address2;

    public void setCryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .lastName(lastName)
                .firstName(firstName)
                .birth(birth)
                .email(email)
                .phoneNum(phoneNum)
                .telNum(telNum)
                .address1(address1)
                .address2(address2)
                .blockUserList(new ArrayList<>())
                .totalArticleCount(0)
                .totalReplyCount(0)
                .lastLoginTime(LocalDateTime.now())
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .userGrade(UserGrade.BRONZE)
                .userPoint(0)
                .articleList(new ArrayList<>())
                .build();
    }
}
