package com.example.bckndApi.data;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor

public class DataSensorController {
    private final DataService dataService;

    @PostMapping("/create")
    public void createData(@RequestBody DataSensor data){
        dataService.createData(data);
    }

}
