package com.example.bckndApi.controller;

import com.example.bckndApi.data.Measure;
import com.example.bckndApi.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

@RestController
@CrossOrigin(maxAge = 3600)

public class MeasureController {

    @Autowired
    private MeasureRepository measureRepository;


    private Measure lastMeasure=null;


    @PostMapping("measure")
    public ResponseEntity<?> addMeasure(@RequestBody Measure measure) {
            measureRepository.save(measure);
            this.lastMeasure=measure;
            var response = new HashMap<String, String>();
            response.put("message", "Operación realizada");
            return ResponseEntity.status(200).body(response);
    }

    @GetMapping("evaluation/{id}")
    public ResponseEntity<?> getMeasureById(@PathVariable("id") long id) {
        var measure = measureRepository.findById(id);
        if(measure.isPresent()){
            return ResponseEntity.status(200).body(measure.get());
        } else {
            var response = new HashMap<String, String>();
            response.put("message", "La muestra no se encontró");
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("measureGetId")
    public ResponseEntity<?> getIdMeasure() {
        if (lastMeasure != null) {
            return ResponseEntity.status(200).body(lastMeasure.getId());
        } else {
            var response = new HashMap<String, String>();
            response.put("message", "No hay medidas disponibles");
            return ResponseEntity.status(404).body(response);
        }
    }
}
