package com.example.bckndApi.repository;

import com.example.bckndApi.data.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByCedula(String cedula);
    @Transactional
    void deleteByCedula(String cedula);
}




