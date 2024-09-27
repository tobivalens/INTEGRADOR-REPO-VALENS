package com.example.bckndApi.data;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataSensorRepository dataRepo;

    public void createData( DataSensor data){
        dataRepo.save(data);
    }
}
