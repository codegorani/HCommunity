package org.hcom.models.user.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResetPasswordRequestDTO {

    private String currentPassword;
    private String newPassword;
}
