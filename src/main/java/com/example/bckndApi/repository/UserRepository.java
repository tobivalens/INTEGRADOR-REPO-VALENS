package com.example.bckndApi.repository;

import com.example.bckndApi.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> , JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByCedula(String cedula);
    @Transactional
    void deleteByCedula(String cedula);
    boolean existsByCedula(String cedula);

}