package org.hcom.models.user.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hcom.models.user.QUser;
import org.hcom.models.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public User findByUsername(String username) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.username.eq(username))
                .fetchOne();
    }
}
