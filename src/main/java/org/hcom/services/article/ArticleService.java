package org.hcom.services.article;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.article.dtos.response.ArticleDetailResponseDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long articleSaveService(ArticleSaveRequestDTO requestDTO, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        return articleRepository.save(requestDTO.toEntity(user)).getIdx();
    }

    @Transactional
    public ArticleDetailResponseDTO articleViewService(Long idx) {
        Article article = articleRepository.findById(idx).orElseThrow(IllegalArgumentException::new);
        return new ArticleDetailResponseDTO(article);
    }

    @Transactional
    public Page<ArticleListResponseDTO> getArticleListAsPage(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Article> articlePage = articleRepository.findAll(pageRequest);
        return articlePage.map(ArticleListResponseDTO::new);
    }
}
