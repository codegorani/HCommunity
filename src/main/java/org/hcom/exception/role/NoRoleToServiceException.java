package org.hcom.exception.role;

import org.hcom.models.user.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoRoleToServiceException  extends RuntimeException {

    public NoRoleToServiceException(UserRole serviceRole, UserRole currentRole) {
        super("Need Role:[" + serviceRole.name() + "] but it was [" + currentRole.name() + "]");
    }
}
