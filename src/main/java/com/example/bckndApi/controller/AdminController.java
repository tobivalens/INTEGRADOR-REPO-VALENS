package com.example.bckndApi.controller;

import com.example.bckndApi.data.Admin;
import com.example.bckndApi.data.Doctor;
import com.example.bckndApi.repository.AdminRepository;
import com.example.bckndApi.repository.DoctorRepository;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("admin")
@CrossOrigin(maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Login para administradores
    @PostMapping("login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        if ("user".equals(admin.getUsername()) && "password".equals(admin.getPassword())) {
            var token = JwtUtil.generateToken(admin.getUsername());
            return ResponseEntity.status(200).body(Map.of(
                    "access_token", token
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "El administrador con ese nombre de usuario o contraseña no existe"
            ));
        }
    }

    // Ver la lista de doctores
    @GetMapping("doctors/list")
    public ResponseEntity<?> listDoctors(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                var doctors = doctorRepository.findAll();
                return ResponseEntity.status(200).body(doctors);
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    // Agregar un nuevo doctor
    @PostMapping("doctors/add")
    public ResponseEntity<?> addDoctor(@RequestHeader("Authorization") String authorization, @RequestBody Doctor doctor) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                doctorRepository.save(doctor);
                return ResponseEntity.status(200).body(Map.of("message", "Doctor added successfully"));
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    // Editar un doctor
    @PutMapping("doctors/edit/{id}")
    public ResponseEntity<?> editDoctor(@RequestHeader("Authorization") String authorization, @PathVariable long id, @RequestBody Doctor updatedDoctor) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                var doctorOpt = doctorRepository.findById(id);
                if (doctorOpt.isPresent()) {
                    Doctor doctor = doctorOpt.get();
                    doctor.setName(updatedDoctor.getName());
                    doctor.setLastname(updatedDoctor.getLastname());
                    doctor.setEmail(updatedDoctor.getEmail());
                    doctor.setCedula(updatedDoctor.getCedula());
                    doctorRepository.save(doctor);
                    return ResponseEntity.status(200).body(Map.of("message", "Doctor updated successfully"));
                }
                return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    // Eliminar un doctor
    @DeleteMapping("doctors/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@RequestHeader("Authorization") String authorization, @PathVariable long id) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                var doctorOpt = doctorRepository.findById(id);
                if (doctorOpt.isPresent()) {
                    doctorRepository.deleteById(id);
                    return ResponseEntity.status(200).body(Map.of("message", "Doctor deleted successfully"));
                }
                return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }
}
