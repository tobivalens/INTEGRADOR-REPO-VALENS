package com.example.bckndApi.controller;

import com.example.bckndApi.data.*;
import com.example.bckndApi.repository.MeasureRepository;
import com.example.bckndApi.repository.UserRepository;
import com.example.bckndApi.util.JwtUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class MeasureController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private UserRepository userRepository;

    private Measure lastMeasure = null;

    @PostMapping("measure")
    public ResponseEntity<?> addMeasure(@RequestBody Measure measure) {
        measureRepository.save(measure);
        this.lastMeasure=measure;
        var response = new HashMap<String, String>();
        response.put("message", "Operación realizada");
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("evaluation/{id}")
    public ResponseEntity<?> getMeasureById(@RequestHeader("Authorization") String authorization, @PathVariable("id") long id) {
        if (validateToken(authorization)) {
            var measure = measureRepository.findById(id);
            if (measure.isPresent()) {
                return ResponseEntity.status(200).body(measure.get());
            } else {
                var response = new HashMap<String, String>();
                response.put("message", "La muestra no se encontró");
                return ResponseEntity.status(404).body(response);
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @GetMapping("measureGetId")
    public ResponseEntity<?> getIdMeasure(@RequestHeader("Authorization") String authorization) {
        if (validateToken(authorization)) {
            if (lastMeasure != null) {
                return ResponseEntity.status(200).body(lastMeasure.getId());
            } else {
                var response = new HashMap<String, String>();
                response.put("message", "No hay medidas disponibles");
                return ResponseEntity.status(404).body(response);
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @PostMapping("/patient/setData")
    public ResponseEntity<String> setPatientData(@RequestBody PatientData patientData) {
        if (lastMeasure != null) {
            Optional<User> optionalUser = userRepository.findByCedula(patientData.getCedula());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                lastMeasure.setUser(user);
                if(patientData.getState().equals("off"))
                {
                    lastMeasure.setState(State.OFF);
                }else{
                    lastMeasure.setState(State.ON);
                }

                if(patientData.getLastMedicine().equals("zapateo")){
                    lastMeasure.setMeasureType(MeasureType.TAPPING);

                }else{
                    lastMeasure.setMeasureType(MeasureType.CLICKING);

                }

                measureRepository.save(lastMeasure);

                return ResponseEntity.ok("Datos asignados exitosamente a la última medida guardada");
            } else {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(404).body("No hay una medida guardada para actualizar");
        }
    }

    @GetMapping("/measures/{id}")
    public ResponseEntity<?> getMeasureById(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        if (validateToken(authorization)) {
            Optional<Measure> measure = measureRepository.findById(id);
            if (measure.isPresent()) {
                return ResponseEntity.ok(measure.get());
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Medición no encontrada"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    // Método para validar el token
    private boolean validateToken(String authorization) {
        if (authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }



    @GetMapping("evaluation/{id}/fft")
    public ResponseEntity<?> getEvaluationByFft(@PathVariable("id") long id){
        HttpHeaders headers = new HttpHeaders();
        var evaluation = measureRepository.findByid(id);
        if (evaluation.isPresent()){
            var readings = evaluation.get().getReadings();
            var json = new Gson().toJson(readings);
            headers.set("Content-Type", "application/json; UTF-8");
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("http://analitica:8000/fft",
                    request,
                    String.class
            );
            return ResponseEntity.status(200).
                    header("contest-Type", "aplication/Json").
                    body(response.getBody());
        }else{
            return ResponseEntity.status(404).body("la muestra no está");
        }

    }



    @GetMapping("evaluation/{id}/autocorrelation")
    public ResponseEntity<?> autocorrelation(@PathVariable("id") long id){
        HttpHeaders headers = new HttpHeaders();
        var evaluation = measureRepository.findByid(id);
        if (evaluation.isPresent()){
            var readings = evaluation.get().getReadings();
            var json = new Gson().toJson(readings);
            headers.set("Content-Type", "application/json; UTF-8");
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("http://analitica:8000/autocorrelacion",
                    request,
                    String.class
            );
            return ResponseEntity.status(200).
                    header("contest-Type", "aplication/Json").
                    body(response.getBody());
        }else{
            return ResponseEntity.status(404).body("la muestra no está");
        }

    }

}
