package org.hcom.models.user.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hcom.models.user.enums.UserGrade;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserGradeRequestDTO {

    private final UserGrade userGrade;
}
