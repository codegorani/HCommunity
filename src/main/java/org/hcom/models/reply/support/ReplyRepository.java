package org.hcom.models.reply.support;

import org.hcom.models.article.Article;
import org.hcom.models.reply.Reply;
import org.hcom.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByArticle(Article article);

    void deleteAllByArticle(Article article);

    void deleteAllByUser(User user);

    Long countAllByArticle(Article article);
}
