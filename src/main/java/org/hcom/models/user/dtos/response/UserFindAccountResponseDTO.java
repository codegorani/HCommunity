package org.hcom.models.user.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindAccountResponseDTO {
    private String username;
    private String result;

    @Builder
    public UserFindAccountResponseDTO(String username, String result) {
        this.username = username;
        this.result = result;
    }
}
