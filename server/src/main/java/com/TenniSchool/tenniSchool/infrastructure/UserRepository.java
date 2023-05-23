package com.TenniSchool.tenniSchool.infrastructure;

import com.TenniSchool.tenniSchool.domain.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    void save(User user);

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    boolean existsByEmail(String email);
}
