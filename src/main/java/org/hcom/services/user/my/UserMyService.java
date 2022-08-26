package org.hcom.services.user.my;

import lombok.RequiredArgsConstructor;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.response.ArticleListResponseByUserDTO;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        if(search != null) {
            articlePage = articleRepository.findAllByUserAndTitleContains(user, search, pageRequest);
        } else {
            articlePage = articleRepository.findAllByUser(user, pageRequest);
        }
        Page<ArticleListResponseByUserDTO> result = articlePage.map(ArticleListResponseByUserDTO::new);
        for(ArticleListResponseByUserDTO dto : result) {
            Article article = articleRepository.findById(dto.getIdx()).orElseThrow(IllegalArgumentException::new);
            dto.setAllReply(replyRepository.countAllByArticle(article));
            dto.setAllLike(likeRepository.countAllByArticle(article));
        }
        return result;
    }
}
