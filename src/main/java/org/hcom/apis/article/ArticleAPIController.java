package org.hcom.apis.article;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.exception.user.NotLoginUserException;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.like.dtos.request.LikeDTO;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
@RestController
public class ArticleAPIController {

    private final ArticleService articleService;

    @PostMapping("/api/v1/article")
    public Long articleSaveControl(@RequestBody ArticleSaveRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw NotLoginUserException();
        }
        return articleService.articleSaveService(requestDTO, sessionUser);
    }

    @PostMapping("/api/v1/article/like")
    public void articleLikeControl(@RequestBody LikeDTO likeDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        articleService.articleLikeService(likeDTO, sessionUser);
    }

    @PostMapping("/api/v1/article/dislike")
    public void articleDislikeControl(@RequestBody LikeDTO likeDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        articleService.articleDislikeService(likeDTO, sessionUser);
    }

    @DeleteMapping("/api/v1/article/{idx}")
    public void articleDeleteControl(@PathVariable("idx") Long idx, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new NotLoginUserException();
        }
        articleService.articleDeleteByArticleIdxService(idx, sessionUser);
    }
}
