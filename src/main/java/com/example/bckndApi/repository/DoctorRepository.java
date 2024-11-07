package com.example.bckndApi.repository;

import com.example.bckndApi.data.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> findByEmailAndPassword(String email, String hashedPass);
}
