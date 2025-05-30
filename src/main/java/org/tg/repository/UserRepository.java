package org.tg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tg.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
