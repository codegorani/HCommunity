package org.hcom.models.article.dtos.response;

import lombok.*;
import org.hcom.models.article.Article;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponseDTO {
    private Long idx;
    private String title;
    private String username;
    private String createdDate;

    public ArticleListResponseDTO(Article article) {
        this.idx = article.getIdx();
        this.title = article.getTitle();
        this.username = article.getUser().getUsername();
        this.createdDate = article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
