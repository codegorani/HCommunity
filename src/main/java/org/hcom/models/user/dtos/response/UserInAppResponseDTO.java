package org.hcom.models.user.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserInAppResponseDTO {

    private final String nickname;
    private final int userPoint;
    private final UserGrade userGrade;
    private final UserRole userRole;
    private final List<User> blockUserList;
    private final LocalDateTime lastLoginTime;
    private final int totalArticleCount;
    private final int totalReplyCount;

    public UserInAppResponseDTO(User user) {
        this.nickname = user.getNickname();
        this.userPoint = user.getUserPoint();
        this.userGrade = user.getUserGrade();
        this.userRole = user.getUserRole();
        this.blockUserList = user.getBlockUserList();
        this.lastLoginTime = user.getLastLoginTime();
        this.totalArticleCount = user.getTotalArticleCount();
        this.totalReplyCount = user.getTotalReplyCount();
    }
}
