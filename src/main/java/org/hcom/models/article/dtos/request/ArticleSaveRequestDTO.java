package org.hcom.models.article.dtos.request;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSaveRequestDTO {

    private String title;
    private String contents;

    public Article toEntity(User user) {
        return Article.builder()
                .title(title)
                .contents(contents)
                .user(user)
                .build();
    }
}
