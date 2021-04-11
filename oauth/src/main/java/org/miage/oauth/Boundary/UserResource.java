package org.miage.oauth.Boundary;

import org.miage.oauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResource extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
