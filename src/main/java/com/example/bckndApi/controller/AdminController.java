package com.example.bckndApi.controller;

import com.example.bckndApi.data.Admin;
import com.example.bckndApi.data.Doctor;
import com.example.bckndApi.repository.AdminRepository;
import com.example.bckndApi.repository.DoctorRepository;
import com.example.bckndApi.util.InvalidAuthException;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.PasswordHasher;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("admin")
@CrossOrigin(maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

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
        try {
            JwtUtil.validateAuthorization(authorization);
            var doctors = doctorRepository.findAll();
            return ResponseEntity.status(200).body(doctors);
        } catch (InvalidAuthException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        } catch (ExpiredJwtException exp) {
            return ResponseEntity.status(401).body(Map.of("message", "Expired token"));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Fallo genérico"));
        }
    }

    // Agregar un nuevo doctor
    @PostMapping("/doctors/add")
    public ResponseEntity<?> addDoctor(@RequestHeader("Authorization") String authorization, @RequestBody Doctor doctor) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);

                // Validar que todos los campos del doctor estén presentes (puedes agregar más validaciones si es necesario)
                if (doctor.getName() == null || doctor.getLastname() == null || doctor.getEmail() == null ||
                        doctor.getCedula() == null || doctor.getPhone() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "All fields are required"));
                }

                doctorRepository.save(doctor);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Doctor added successfully"));
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }
    }


    @Transactional
    @PutMapping("/doctors/update/{cedula}")
    public ResponseEntity<?> updateDoctor(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("cedula") String cedula,
            @RequestBody Doctor updatedDoctorDetails) {

        try {

            JwtUtil.validateAuthorization(authorization);
            Optional<Doctor> optionalDoctor = doctorRepository.findByCedula(cedula);

            if (optionalDoctor.isPresent()) {
                Doctor doctor = optionalDoctor.get();

                doctor.setName(updatedDoctorDetails.getName());
                doctor.setLastname(updatedDoctorDetails.getLastname());
                doctor.setEmail(updatedDoctorDetails.getEmail());
                doctor.setPhone(updatedDoctorDetails.getPhone());
                doctor.setPassword(updatedDoctorDetails.getPassword());

                doctorRepository.save(doctor);
                return ResponseEntity.status(HttpStatus.OK).body("Doctor actualizado satisfactoriamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor no encontrado.");
            }

        } catch (InvalidAuthException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        } catch (ExpiredJwtException exp) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Expired token"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Fallo genérico"));
        }
    }


    @Transactional
    @DeleteMapping("/doctors/delete/{cedula}")
    public ResponseEntity<?> deleteDoctor(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("cedula") String cedula) {

        try {
            JwtUtil.validateAuthorization(authorization);
            Optional<Doctor> doctor = doctorRepository.findByCedula(cedula);

            if (doctor.isPresent()) {
                doctorRepository.deleteByCedula(cedula);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Doctor eliminado satisfactoriamente."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Doctor no encontrado."));
            }

        } catch (InvalidAuthException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized"));
        } catch (ExpiredJwtException exp) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Expired token"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Fallo genérico"));
        }
    }

}

