package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface UserRepository
        extends JpaRepository<User, Long> {
    List<User> findByNameContaining(String name, Sort sort);
}
