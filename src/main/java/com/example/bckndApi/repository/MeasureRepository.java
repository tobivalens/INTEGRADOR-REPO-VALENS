package com.example.bckndApi.repository;

import com.example.bckndApi.data.Measure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findByUserCedula(String cedula);  // Método para obtener mediciones por cédula del usuario
    Optional<Measure> findByid(long id);
}
