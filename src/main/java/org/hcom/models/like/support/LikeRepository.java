package org.hcom.models.like.support;

import org.hcom.models.article.Article;
import org.hcom.models.like.Like;
import org.hcom.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Long countAllByUserAndArticle(User user, Article article);

    void deleteByUserAndArticle(User user, Article article);

    Long countAllByArticle(Article article);

    void deleteAllByUser(User user);

    void deleteAllByArticle(Article article);

    List<Like> findAllByUser(User user);

    Optional<Like> findByUserAndArticle(User user, Article article);
}
