package org.hcom.models.like;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.common.BaseTimeEntity;
import org.hcom.models.like.dtos.response.LikeListResponseByUserDTO;
import org.hcom.models.user.User;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "h_like")
public class Like extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    public LikeListResponseByUserDTO toLikeList() {
        return new LikeListResponseByUserDTO(this);
    }
}
