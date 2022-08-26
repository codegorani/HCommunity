package org.hcom.models.like.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    private Long idx;
    private Long user;
}
