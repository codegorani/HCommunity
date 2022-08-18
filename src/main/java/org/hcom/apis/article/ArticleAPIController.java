package org.hcom.apis.article;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
@RestController
public class ArticleAPIController {

    private final ArticleService articleService;

    @PostMapping("/article/save")
    public Long articleSaveControl(@RequestBody ArticleSaveRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        return articleService.articleSaveService(requestDTO, sessionUser);
    }
}
