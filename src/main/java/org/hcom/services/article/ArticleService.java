package org.hcom.services.article;

import lombok.RequiredArgsConstructor;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.article.dtos.response.ArticleDetailResponseDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseByUserDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.like.Like;
import org.hcom.models.like.dtos.request.LikeDTO;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.Reply;
import org.hcom.models.reply.dtos.request.ReplySaveRequestDTO;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public Long articleSaveService(ArticleSaveRequestDTO requestDTO, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        return articleRepository.save(requestDTO.toEntity(user)).getIdx();
    }

    @Transactional
    public ArticleDetailResponseDTO articleViewService(Long idx, SessionUser sessionUser) {
        User user = null;
        Article article = articleRepository.findById(idx).orElseThrow(IllegalArgumentException::new);
        List<Reply> replyList = replyRepository.findAllByArticle(article);
        ArticleDetailResponseDTO responseDTO = new ArticleDetailResponseDTO(article, replyList);
        if(sessionUser != null) {
            user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
            if(user != null) {
                responseDTO.setIsLike(likeRepository.countAllByUserAndArticle(user, article));
            } else {
                responseDTO.setIsLike(0L);
            }
        } else {
            responseDTO.setIsLike(0L);
        }
        responseDTO.setAllLike(likeRepository.countAllByArticle(article));
        return responseDTO;
    }

    @Transactional
    public Page<ArticleListResponseDTO> getArticleListAsPage(int page, SessionUser sessionUser, String search) {
        User user = null;
        Page<Article> articlePage;
        if(sessionUser != null) {
            user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        }
        PageRequest pageRequest = PageRequest.of(page, 10);
        if(search != null) {
            articlePage = articleRepository.findAllByTitleContains(search, pageRequest);
        } else {
            articlePage = articleRepository.findAll(pageRequest);
        }
        Page<ArticleListResponseDTO> result = articlePage.map(ArticleListResponseDTO::new);
        for(ArticleListResponseDTO dto : result) {
            Article article = articleRepository.findById(dto.getIdx()).orElseThrow(IllegalArgumentException::new);
            if(user != null) {
                dto.setIsLike(likeRepository.countAllByUserAndArticle(user, article));
            } else {
                dto.setIsLike(0L);
            }
            dto.setAllLike(likeRepository.countAllByArticle(article));
            dto.setAllReply(replyRepository.countAllByArticle(article));
        }
        return result;
    }

    @Transactional
    public void articleLikeService(LikeDTO likeDTO, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository.findById(likeDTO.getIdx()).orElseThrow(IllegalArgumentException::new);
        likeRepository.save(Like.builder().user(user).article(article).build());
    }

    @Transactional
    public void articleDislikeService(LikeDTO likeDTO, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository.findById(likeDTO.getIdx()).orElseThrow(IllegalArgumentException::new);
        likeRepository.deleteByUserAndArticle(user, article);
    }

    @Transactional
    public void articleDeleteByArticleIdxService(Long articleIdx, SessionUser sessionUser) {
        Article article = articleRepository.findById(articleIdx).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        if(!article.getUser().getIdx().equals(user.getIdx())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        likeRepository.deleteAllByArticle(article);
        replyRepository.deleteAllByArticle(article);
        articleRepository.delete(article);
    }

    @Transactional
    public void replySaveService(SessionUser sessionUser, ReplySaveRequestDTO requestDTO) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository.findById(requestDTO.getIdx()).orElseThrow(IllegalArgumentException::new);
        replyRepository.save(requestDTO.toEntity(article, user));
    }

    @Transactional
    public void replyDeleteService(Long idx, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Reply reply = replyRepository.findById(idx).orElseThrow(IllegalArgumentException::new);
        if (!reply.getUser().getIdx().equals(user.getIdx())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        replyRepository.delete(reply);
    }

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
