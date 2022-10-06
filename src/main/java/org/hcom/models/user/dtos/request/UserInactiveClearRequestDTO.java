package org.hcom.models.user.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserInactiveClearRequestDTO {
    private String username;
    private String password;
    private String email;
}
