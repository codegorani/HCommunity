package org.hcom.services.article;

import lombok.RequiredArgsConstructor;
import org.hcom.exception.article.NoSuchArticleFoundException;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.request.ArticleSaveRequestDTO;
import org.hcom.models.article.dtos.response.ArticleDetailResponseDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.like.Like;
import org.hcom.models.like.dtos.request.LikeDTO;
import org.hcom.models.like.support.LikeRepository;
import org.hcom.models.reply.Reply;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        user.modifyUserPoint(1000);
        user.setTotalArticleCount(user.getTotalArticleCount() + 1);
        userRepository.save(user);
        return articleRepository.save(requestDTO.toEntity(user)).getIdx();
    }

    @Transactional
    public ArticleDetailResponseDTO articleViewService(Long idx, SessionUser sessionUser) {
        User user = null;
        Article article = articleRepository.findById(idx).orElseThrow(NoSuchArticleFoundException::new);
        List<Reply> replyList = replyRepository.findAllByArticle(article);
        ArticleDetailResponseDTO responseDTO = new ArticleDetailResponseDTO(article, replyList);
        if(sessionUser != null) {
            user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
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
            user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        }
        PageRequest pageRequest = PageRequest.of(page, 10);
        if(search != null) {
            articlePage = articleRepository.findAllByTitleContains(search, pageRequest);
        } else {
            articlePage = articleRepository.findAll(pageRequest);
        }
        Page<ArticleListResponseDTO> result = articlePage.map(ArticleListResponseDTO::new);
        for(ArticleListResponseDTO dto : result) {
            Article article = articleRepository.findById(dto.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
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
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        Article article = articleRepository.findById(likeDTO.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
        likeRepository.save(Like.builder().user(user).article(article).build());
        User writer = article.getUser();
        if(!writer.getNickname().equals(user.getNickname())) {
            writer.modifyUserPoint(30);
        }
        userRepository.save(writer);
    }

    @Transactional
    public void articleDislikeService(LikeDTO likeDTO, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        Article article = articleRepository.findById(likeDTO.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
        likeRepository.deleteByUserAndArticle(user, article);
        User writer = article.getUser();
        if(!writer.getNickname().equals(user.getNickname())) {
            writer.modifyUserPoint(-30);
        }
        userRepository.save(writer);
    }

    @Transactional
    public void articleDeleteByArticleIdxService(Long articleIdx, SessionUser sessionUser) {
        Article article = articleRepository.findById(articleIdx).orElseThrow(NoSuchArticleFoundException::new);
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchArticleFoundException::new);
        if(!article.getUser().getIdx().equals(user.getIdx())) {
            throw new NoPermissionException();
        }
        user.modifyUserPoint(-900);
        user.setTotalArticleCount(user.getTotalArticleCount() - 1);
        userRepository.save(user);
        likeRepository.deleteAllByArticle(article);
        replyRepository.deleteAllByArticle(article);
        articleRepository.delete(article);
    }
}
