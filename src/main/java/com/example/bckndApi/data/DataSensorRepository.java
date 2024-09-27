package com.example.bckndApi.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSensorRepository extends JpaRepository <DataSensor, Integer> {

}
