package org.hcom.models.user;

import com.google.common.collect.Lists;
import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.common.BaseTimeEntity;
import org.hcom.models.user.admin.request.AdminModifyRequestDTO;
import org.hcom.models.user.dtos.request.UserModifyRequestDTO;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserRole;
import org.hcom.models.user.enums.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "h_user")
public class User extends BaseTimeEntity {

    // DB Index
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    //========== User in-app data ==========

    // User id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private int userPoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade userGrade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(fetch = FetchType.EAGER)
    private List<User> blockUserList;

    @Column(nullable = false)
    private LocalDateTime lastLoginTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    private int totalArticleCount;

    @Column(nullable = false)
    private int totalReplyCount;

    //========== User personal data ==========

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    // Format: yyyy-MM-dd
    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNum;

    @Column
    private String telNum;

    // General Address
    @Column(nullable = false)
    private String address1;

    // Detail Address
    @Column
    private String address2;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articleList = Lists.newArrayList();

    // update password
    public void passwordReset(String newPassword) {
        this.password = newPassword;
    }

    public void modifyUserPoint(int point) {
        this.userPoint += point;

        if (this.userPoint < 2000) {
            this.userGrade = UserGrade.BRONZE;
        } else if(this.userPoint < 8000) {
            this.userGrade = UserGrade.SILVER;
        } else if(this.userPoint < 20000) {
            this.userGrade = UserGrade.GOLD;
        } else if(this.userPoint < 35000) {
            this.userGrade = UserGrade.PLATINUM;
        } else if(this.userPoint < 69000) {
            this.userGrade = UserGrade.DIAMOND;
        } else if(this.userPoint < 125000) {
            this.userGrade = UserGrade.MASTER;
        } else if(this.userPoint < 235000) {
            this.userGrade = UserGrade.GRANDMASTER;
        } else {
            this.userGrade = UserGrade.CHALLENGER;
        }
    }

    // modify personal data
    public void modifyByUser(UserModifyRequestDTO requestDTO) {
        this.lastName = requestDTO.getLastName();
        this.firstName = requestDTO.getFirstName();
        this.birth = requestDTO.getBirth();
        this.email = requestDTO.getEmail();
        this.telNum = requestDTO.getTelNum();
        this.phoneNum = requestDTO.getPhoneNum();
        this.address1 = requestDTO.getAddress1();
        this.address2 = requestDTO.getAddress2();
    }

    public void modifyByAdmin(AdminModifyRequestDTO requestDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.userGrade = requestDTO.getUserGrade();
        this.userPoint = requestDTO.getUserPoint();
        this.userRole = requestDTO.getUserRole();
        this.userStatus = requestDTO.getUserStatus();
        this.username = requestDTO.getUsername();
        this.nickname = requestDTO.getNickname();
        this.password = passwordEncoder.encode(requestDTO.getPassword());
    }

    public User inactive() {
        this.userStatus = UserStatus.INACTIVE;
        return this;
    }
}
