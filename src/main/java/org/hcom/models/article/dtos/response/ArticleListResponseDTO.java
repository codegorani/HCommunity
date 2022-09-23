package org.hcom.models.article.dtos.response;

import lombok.*;
import org.hcom.models.article.Article;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponseDTO {
    private Long idx;
    private String title;
    private String nickname;
    private String createdDate;
    private Long isLike;
    private Long allLike;
    private Long allReply;
    private int view;

    public ArticleListResponseDTO(Article article) {
        this.idx = article.getIdx();
        this.title = article.getTitle();
        this.nickname = article.getUser().getNickname();
        this.createdDate = article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.view = article.getView();
    }
}
