package org.hcom.models.article.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class ArticleDataResponseDTO {

    private String galleryName;
    private Page<ArticleListResponseDTO> articleList;
}
