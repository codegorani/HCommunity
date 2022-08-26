package org.hcom.services.reply;

import lombok.RequiredArgsConstructor;
import org.hcom.models.article.Article;
import org.hcom.models.article.support.ArticleRepository;
import org.hcom.models.reply.Reply;
import org.hcom.models.reply.dtos.request.ReplySaveRequestDTO;
import org.hcom.models.reply.support.ReplyRepository;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.support.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

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
}
