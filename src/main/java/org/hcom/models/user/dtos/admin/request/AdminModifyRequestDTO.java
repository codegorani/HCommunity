package org.hcom.models.user.dtos.admin.request;

import lombok.*;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AdminModifyRequestDTO {

    private UserGrade userGrade;
    private int userPoint;
    private UserRole userRole;
    private UserStatus userStatus;
    private String username;
    private String nickname;
    private String password;
}
