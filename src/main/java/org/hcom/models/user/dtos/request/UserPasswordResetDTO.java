package org.hcom.models.user.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPasswordResetDTO {

    private String username;
    private String newPassword;
}
