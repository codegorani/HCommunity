package org.hcom.models.user.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.hcom.models.user.User;

@Getter
@Setter
public class UserPersonalResponseDTO {

    private String lastName;
    private String firstName;
    private String birth;
    private String email;
    private String phoneNum;
    private String telNum;
    private String address1;
    private String address2;

    public UserPersonalResponseDTO(User user) {
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.birth = user.getBirth();
        this.email = user.getEmail();
        this.phoneNum = user.getPhoneNum();
        this.telNum = user.getTelNum();
        this.address1 = user.getAddress1();
        this.address2 = user.getAddress2();
    }
}
