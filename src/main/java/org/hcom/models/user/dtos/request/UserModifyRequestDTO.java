package org.hcom.models.user.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserModifyRequestDTO {
    private String lastName;
    private String firstName;
    private String birth;
    private String email;
    private String telNum;
    private String phoneNum;
    private String address1;
    private String address2;
}
