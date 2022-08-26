package org.hcom.models.article.support;

import org.hcom.models.article.Article;
import org.hcom.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByTitleContains(String title, Pageable pageable);

    Page<Article> findAllByUserAndTitleContains(User user, String title, Pageable pageable);

    Page<Article> findAllByUser(User user, Pageable pageable);
}
