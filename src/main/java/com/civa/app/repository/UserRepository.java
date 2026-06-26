
package com.civa.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civa.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
}