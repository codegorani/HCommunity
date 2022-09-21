package org.hcom.models.article;

import lombok.*;
import org.hcom.models.article.enums.ArticleType;
import org.hcom.models.common.BaseTimeEntity;
import org.hcom.models.gallery.Gallery;
import org.hcom.models.user.User;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "h_article")
public class Article extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private ArticleType articleType;

    @ManyToOne
    private Gallery gallery;

}
