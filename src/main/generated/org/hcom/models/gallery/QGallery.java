package org.hcom.models.gallery;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGallery is a Querydsl query type for Gallery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGallery extends EntityPathBase<Gallery> {

    private static final long serialVersionUID = 2073399933L;

    public static final QGallery gallery = new QGallery("gallery");

    public final org.hcom.models.common.QBaseTimeEntity _super = new org.hcom.models.common.QBaseTimeEntity(this);

    public final ListPath<org.hcom.models.article.Article, org.hcom.models.article.QArticle> articleList = this.<org.hcom.models.article.Article, org.hcom.models.article.QArticle>createList("articleList", org.hcom.models.article.Article.class, org.hcom.models.article.QArticle.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath galleryKorName = createString("galleryKorName");

    public final StringPath galleryName = createString("galleryName");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public QGallery(String variable) {
        super(Gallery.class, forVariable(variable));
    }

    public QGallery(Path<? extends Gallery> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGallery(PathMetadata metadata) {
        super(Gallery.class, metadata);
    }

}

