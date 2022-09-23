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
    private String nickname;
    private String title;
    private String contents;
    private String createdDate;
    private Long allReply;
    private Long allLike;
    private int view;

    public ArticleListResponseByUserDTO(Article article) {
        this.idx = article.getIdx();
        this.nickname = article.getUser().getNickname();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdDate = article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.view = article.getView();
    }
}
