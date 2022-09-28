package org.hcom.models.user.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequestDTO {

    @Pattern(regexp = "^[a-zA-z0-9]{4,12}$", message = "USERNAME_PATTERN_IS_INVALID")
    @NotBlank(message = "USERNAME_IS_MANDATORY")
    private String username;

    @Size(min = 8, max = 13)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@!%*#?&]{8,}$", message = "PASSWORD_PATTERN_IS_INVALID")
    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    @NotBlank(message = "NICKNAME_IS_MANDATORY")
    private String nickname;

    @NotBlank(message = "LASTNAME_IS_MANDATORY")
    private String lastName;

    @NotBlank(message = "FIRST_NAME_IS_MANDATORY")
    private String firstName;

    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "BIRTH_PATTERN_IS_INVALID")
    @NotBlank(message = "BIRTH_IS_MANDATORY")
    private String birth;

    @Email(message = "EMAIL_PATTERN_IS_INVALID")
    @NotBlank(message = "EMAIL_IS_MANDATORY")
    private String email;

    @Pattern(regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", message = "PHONE_NUM_PATTERN_IS_INVALID")
    @NotBlank(message = "PHONE_NUM_IS_MANDATORY")
    private String phoneNum;

    @Pattern(regexp = "^$|^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", message = "TEL_NUM_PATTERN_IS_INVALID")
    private String telNum;

    @NotBlank(message = "LOCAL_ADDRESS_IS_MANDATORY")
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
                .build();
    }
}
