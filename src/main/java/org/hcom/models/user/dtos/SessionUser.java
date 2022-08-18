package org.hcom.models.user.dtos;

import lombok.Getter;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SessionUser implements Serializable {

    private final Long idx;
    private final String username;
    private final String nickname;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String birth;
    private final String phoneNum;
    private final String telNum;
    private final String address1;
    private final String address2;
    private final UserRole userRole;
    private final int userPoint;
    private final int totalArticleCount;
    private final int totalReplyCount;
    private final List<User> blockUserList;
    private final LocalDateTime lastLoginTime;
    private final UserGrade userGrade;
    private final UserStatus userStatus;

    public SessionUser(User user) {
        if(user == null) {
            System.out.println("User is NULL");
        }
        assert user != null;
        this.idx = user.getIdx();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birth = user.getBirth();
        this.phoneNum = user.getPhoneNum();
        this.telNum = user.getTelNum();
        this.address1 = user.getAddress1();
        this.address2 = user.getAddress2();
        this.userRole = user.getUserRole();
        this.userPoint = user.getUserPoint();
        this.totalArticleCount = user.getTotalArticleCount();
        this.totalReplyCount = user.getTotalReplyCount();
        this.blockUserList = user.getBlockUserList();
        this.lastLoginTime = user.getLastLoginTime();
        this.userGrade = user.getUserGrade();
        this.userStatus = user.getUserStatus();
    }
}
