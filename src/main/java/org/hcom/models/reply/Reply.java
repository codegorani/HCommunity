package org.hcom.models.reply;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.common.BaseTimeEntity;
import org.hcom.models.user.User;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reply extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    @JoinColumn
    @ManyToOne
    private User user;

    @JoinColumn
    @ManyToOne
    private Article article;

    @Column(columnDefinition = "TEXT")
    private String replyContent;
}
