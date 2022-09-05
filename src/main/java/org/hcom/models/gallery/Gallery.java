package org.hcom.models.gallery;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "h_gallery")
public class Gallery extends BaseTimeEntity {

    @GeneratedValue
    @Id
    private Long idx;

    @Column
    private String galleryName;

    @Column
    private String galleryKorName;

    @Builder.Default
    @OneToMany
    private List<Article> articleList = new ArrayList<>();
}
