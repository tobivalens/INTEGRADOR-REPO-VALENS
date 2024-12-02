package com.example.bckndApi.controller;

import com.example.bckndApi.data.Doctor;
import com.example.bckndApi.repository.DoctorRepository;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(maxAge = 3600)
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("doctors/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        var hashedPass = PasswordHasher.hashPassword(doctor.getPassword());
        doctor.setPassword(hashedPass);
        System.out.println(doctor);
        doctorRepository.save(doctor);
        return ResponseEntity.status(200).body(doctor);
    }

    @PostMapping("doctors/login")
    public ResponseEntity<?> loginDoctor(@RequestBody Doctor doctor) {
        var hashedPass = PasswordHasher.hashPassword(doctor.getPassword());
        var optionalUser = doctorRepository.findByEmailAndPassword(doctor.getEmail(), hashedPass);
        if (optionalUser.isPresent()) {
            var token = JwtUtil.generateToken(doctor.getEmail());
            return ResponseEntity.status(200).body(Map.of("access_token", token));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "El usuario no existe"));
        }
    }

    @GetMapping("doctors/me")
    public ResponseEntity<?> getCurrentDoctor(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                String email = JwtUtil.extractEmailFromToken(token);
                var optionalUser = doctorRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    return ResponseEntity.status(200).body(optionalUser.get());
                } else {
                    return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
                }
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }
}
