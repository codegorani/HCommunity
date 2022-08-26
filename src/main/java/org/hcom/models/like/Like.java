package org.hcom.models.like;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.user.User;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "like_table")
public class Like {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;
}
