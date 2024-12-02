package com.example.bckndApi.controller;

import com.example.bckndApi.data.Diagnostic;
import com.example.bckndApi.data.Measure;
import com.example.bckndApi.data.User;
import com.example.bckndApi.repository.MeasureRepository;
import com.example.bckndApi.repository.UserRepository;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge=3600)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasureRepository measureRepository;  // Repositorio de mediciones


    @PostMapping("users/create")
    public ResponseEntity<?> createUser(@RequestBody User user, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            JwtUtil.validateToken(token);

            if (user.getName() == null || user.getLastname() == null || user.getEmail() == null ||
                    user.getCedula() == null || user.getPassword() == null || user.getBirthday() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Todos los campos son obligatorios"));
            }

            if (userRepository.existsByCedula(user.getCedula())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Ya existe un usuario con la misma cédula"));
            }

            var hashedPass = PasswordHasher.hashPassword(user.getPassword());
            user.setPassword(hashedPass);

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (Exception e) {
            // Si ocurre un error en la validación del token, responder con 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token inválido o expirado"));
        }
    }

    @GetMapping("/users/check-cedula")
    public ResponseEntity<?> checkCedula(@RequestParam String cedula) {
        boolean exists = userRepository.existsByCedula(cedula);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    //user/delete

    @GetMapping("users/allMeasures/{cedula}")
    public ResponseEntity<?> userMeasure(@RequestHeader("Authorization") String authorization, @PathVariable("cedula") String cedula) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                Optional<User> optionalUser = userRepository.findByCedula(cedula);

                if (optionalUser.isPresent()) {
                    List<Measure> measures = measureRepository.findByUserCedula(cedula);

                    if (measures.isEmpty()) {
                        return ResponseEntity.status(404).body(Map.of("message", "No se encontraron mediciones para este usuario"));
                    }

                    return ResponseEntity.status(200).body(measures);
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


    @GetMapping("users/list")
    public ResponseEntity<?> listUser(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token);
                var users = userRepository.findAll();
                return ResponseEntity.status(200).body(users);
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @PostMapping("users/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        var hashedPass = PasswordHasher.hashPassword(user.getPassword());
        var optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), hashedPass);
        if (optionalUser.isPresent()) {
            var token = JwtUtil.generateToken(user.getEmail());
            return ResponseEntity.status(200).body(Map.of(
                    "access_token", token
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "El usuario con ese mail o password no existe"
            ));
        }
    }


    @Transactional
    @DeleteMapping("/patients/delete/{cedula}")
    public ResponseEntity<?> deletePaciente(@RequestHeader("Authorization") String authorization, @PathVariable("cedula") String cedula) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                JwtUtil.validateToken(token); // Validamos el token para la acción
                Optional<User> paciente = userRepository.findByCedula(cedula);

                if (paciente.isPresent()) {
                    userRepository.deleteByCedula(cedula);
                    return ResponseEntity.ok().body("Paciente eliminado correctamente");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
                }
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("message", ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @GetMapping("users/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                String email = JwtUtil.extractEmailFromToken(token); // Extraemos el email del token
                var optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    return ResponseEntity.status(200).body(optionalUser.get());
                } else {
                    return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return ResponseEntity.status(401).body(Map.of("message", ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

}
