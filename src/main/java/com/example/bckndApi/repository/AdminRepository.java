package com.example.bckndApi.repository;


import com.example.bckndApi.data.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndPassword(String username, String password);
}