package org.hcom.models.user.support;

import org.hcom.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsernameAndEmail(String username, String email);

    Optional<User> findByEmail(String email);
}
