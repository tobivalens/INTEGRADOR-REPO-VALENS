package com.example.bckndApi.repository;

import com.example.bckndApi.data.Measure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends CrudRepository<Measure, Long> {

}
