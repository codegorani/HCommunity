package org.hcom.services.user.my;

import lombok.RequiredArgsConstructor;
import org.hcom.exception.article.NoSuchArticleFoundException;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.response.ArticleListResponseByUserDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.like.Like;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMyService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Page<ArticleListResponseByUserDTO> getArticleListByUser(int page, SessionUser sessionUser, String search) {
        Page<Article> articlePage;
        PageRequest pageRequest = PageRequest.of(page, 10);
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        if(search != null) {
            articlePage = articleRepository.findAllByUserAndTitleContains(user, search, pageRequest);
        } else {
            articlePage = articleRepository.findAllByUser(user, pageRequest);
        }
        Page<ArticleListResponseByUserDTO> result = articlePage.map(ArticleListResponseByUserDTO::new);
        for(ArticleListResponseByUserDTO dto : result) {
            Article article = articleRepository.findById(dto.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
            dto.setAllReply(replyRepository.countAllByArticle(article));
            dto.setAllLike(likeRepository.countAllByArticle(article));
        }
        return result;
    }

    @Transactional
    public Page<ArticleListResponseByUserDTO> getLikeListByUser(int page, SessionUser sessionUser, String search) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        List<Like> likeList = likeRepository.findAllByUser(user);
        List<Article> articleList = likeList.stream().map(Like::getArticle).collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page, 10);
        int start = (int)pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), articleList.size());
        Page<Article> articlePage = new PageImpl<>(articleList.subList(start, end), pageRequest, articleList.size());
        Page<ArticleListResponseByUserDTO> result = articlePage.map(ArticleListResponseByUserDTO::new);
        for(ArticleListResponseByUserDTO dto : result) {
            Article article = articleRepository.findById(dto.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
            dto.setAllReply(replyRepository.countAllByArticle(article));
            dto.setAllLike(likeRepository.countAllByArticle(article));
        }
        return result;
    }
}
