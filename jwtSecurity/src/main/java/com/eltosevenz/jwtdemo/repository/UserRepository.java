package com.eltosevenz.jwtdemo.repository;

import com.eltosevenz.jwtdemo.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByUsername(String username);
}
