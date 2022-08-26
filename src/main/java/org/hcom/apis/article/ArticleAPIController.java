package org.hcom.apis.article;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.like.dtos.request.LikeDTO;
import org.hcom.models.reply.dtos.request.ReplySaveRequestDTO;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
@RestController
public class ArticleAPIController {

    private final ArticleService articleService;

    @PostMapping("/api/v1/article")
    public Long articleSaveControl(@RequestBody ArticleSaveRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NOT LOGIN");
        }
        return articleService.articleSaveService(requestDTO, sessionUser);
    }

    @PostMapping("/api/v1/like")
    public void articleLikeControl(@RequestBody LikeDTO likeDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        articleService.articleLikeService(likeDTO, sessionUser);
    }

    @PostMapping("/api/v1/dislike")
    public void articleDislikeControl(@RequestBody LikeDTO likeDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        articleService.articleDislikeService(likeDTO, sessionUser);
    }

    @DeleteMapping("/api/v1/article/{idx}")
    public void articleDeleteControl(@PathVariable("idx") Long idx, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        articleService.articleDeleteByArticleIdxService(idx, sessionUser);
    }

    @PostMapping("/api/v1/reply")
    public void replySaveControl(@RequestBody ReplySaveRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        articleService.replySaveService(sessionUser, requestDTO);
    }

    @DeleteMapping("/api/v1/reply/{replyIdx}")
    public void replyDelete(@PathVariable("replyIdx") Long idx, @LoginUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        articleService.replyDeleteService(idx, sessionUser);
    }
}
