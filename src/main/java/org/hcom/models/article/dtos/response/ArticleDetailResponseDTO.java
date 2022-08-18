package org.hcom.models.article.dtos.response;

import lombok.*;
import org.hcom.models.article.Article;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailResponseDTO {
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdDate;

    public ArticleDetailResponseDTO(Article article) {
        this.username = article.getUser().getUsername();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdDate = article.getCreatedDate();
    }
}
