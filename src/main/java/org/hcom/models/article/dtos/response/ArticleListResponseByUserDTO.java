package org.hcom.models.article.dtos.response;

import lombok.*;
import org.hcom.models.article.Article;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponseByUserDTO {
    private Long idx;
    private String title;
    private String createdDate;
    private Long allReply;
    private Long allLike;

    public ArticleListResponseByUserDTO(Article article) {
        this.idx = article.getIdx();
        this.title = article.getTitle();
        this.createdDate = article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
