package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository
        extends JpaRepository<User, Long> {

    List<User> findByNameContaining(String name, Sort sort);

    Page<User> findByNameContaining(String name, Pageable pageable);

    boolean existsByNameAndAddress(String name, String address);
}
