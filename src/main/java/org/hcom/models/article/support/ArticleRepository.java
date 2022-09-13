package org.hcom.models.article.support;

import org.hcom.models.article.Article;
import org.hcom.models.gallery.Gallery;
import org.hcom.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByTitleContainsAndGallery(String title, Gallery gallery, Pageable pageable);

    Page<Article> findAllByGallery(Gallery gallery, Pageable pageable);

    Page<Article> findAllByUserAndTitleContains(User user, String title, Pageable pageable);

    Page<Article> findAllByUser(User user, Pageable pageable);

    Long countByGallery(Gallery gallery);
}
