package org.hcom.models.article.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcom.models.article.Article;
import org.hcom.models.user.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSaveRequestDTO {

    @NotBlank(message = "TITLE_IS_MANDATORY")
    private String title;

    @NotBlank(message = "CONTENTS_IS_MANDATORY")
    private String contents;

    public Article toEntity(User user) {
        return Article.builder()
                .title(title)
                .contents(contents)
                .user(user)
                .build();
    }
}
