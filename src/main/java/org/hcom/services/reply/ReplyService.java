package org.hcom.services.reply;

import lombok.RequiredArgsConstructor;
import org.hcom.exception.article.NoSuchArticleFoundException;
import org.hcom.exception.reply.NoSuchReplyFoundException;
import org.hcom.exception.user.NoPermissionException;
import org.hcom.exception.user.NoSuchUserFoundException;
import org.hcom.models.article.Article;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.reply.Reply;
import org.hcom.models.reply.dtos.request.ReplySaveRequestDTO;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void replySaveService(SessionUser sessionUser, ReplySaveRequestDTO requestDTO) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        Article article = articleRepository.findById(requestDTO.getIdx()).orElseThrow(NoSuchArticleFoundException::new);
        replyRepository.save(requestDTO.toEntity(article, user));
    }

    @Transactional
    public void replyDeleteService(Long idx, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(NoSuchUserFoundException::new);
        Reply reply = replyRepository.findById(idx).orElseThrow(NoSuchReplyFoundException::new);
        if (!reply.getUser().getIdx().equals(user.getIdx())) {
            throw new NoPermissionException();
        }
        replyRepository.delete(reply);
    }
}
