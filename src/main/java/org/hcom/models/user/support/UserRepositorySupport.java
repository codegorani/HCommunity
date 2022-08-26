package org.hcom.models.user.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hcom.models.user.QUser;
import org.hcom.models.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public User findByUsername(String username) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.username.eq(username))
                .fetchOne();
    }

    public List<User> findAll() {
        return queryFactory
                .selectFrom(QUser.user)
                .fetch();
    }
}
