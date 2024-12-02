package com.example.bckndApi.repository;

import com.example.bckndApi.data.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> findByEmailAndPassword(String email, String hashedPass);
        Optional<Doctor> findByCedula(String cedula);
        @Transactional
        void deleteByCedula(String cedula);
        boolean existsByCedula(String cedula);

    Optional<Doctor> findByEmail(String email);
}
