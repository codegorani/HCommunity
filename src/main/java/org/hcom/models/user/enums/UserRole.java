package org.hcom.models.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER", "일반 유저"),
    MANAGER("ROLE_MANAGER", "관리자"),
    ADMIN("ROLE_ADMIN", "운영자"),
    DEVELOPER("ROLE_DEVELOPER", "개발자");

    private final String role;
    private final String name;
}
