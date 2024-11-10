package com.example.bckndApi.repository;

import com.example.bckndApi.data.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
}
