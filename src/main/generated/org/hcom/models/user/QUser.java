package org.hcom.models.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 214694741L;

    public static final QUser user = new QUser("user");

    public final org.hcom.models.common.QBaseTimeEntity _super = new org.hcom.models.common.QBaseTimeEntity(this);

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final ListPath<org.hcom.models.article.Article, org.hcom.models.article.QArticle> articleList = this.<org.hcom.models.article.Article, org.hcom.models.article.QArticle>createList("articleList", org.hcom.models.article.Article.class, org.hcom.models.article.QArticle.class, PathInits.DIRECT2);

    public final StringPath birth = createString("birth");

    public final ListPath<User, QUser> blockUserList = this.<User, QUser>createList("blockUserList", User.class, QUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginTime = createDateTime("lastLoginTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath lastName = createString("lastName");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNum = createString("phoneNum");

    public final StringPath telNum = createString("telNum");

    public final NumberPath<Integer> totalArticleCount = createNumber("totalArticleCount", Integer.class);

    public final NumberPath<Integer> totalReplyCount = createNumber("totalReplyCount", Integer.class);

    public final EnumPath<org.hcom.models.user.enums.UserGrade> userGrade = createEnum("userGrade", org.hcom.models.user.enums.UserGrade.class);

    public final StringPath username = createString("username");

    public final NumberPath<Integer> userPoint = createNumber("userPoint", Integer.class);

    public final EnumPath<org.hcom.models.user.enums.UserRole> userRole = createEnum("userRole", org.hcom.models.user.enums.UserRole.class);

    public final EnumPath<org.hcom.models.user.enums.UserStatus> userStatus = createEnum("userStatus", org.hcom.models.user.enums.UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

