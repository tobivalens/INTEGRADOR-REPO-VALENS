package com.example.bckndApi.controller;

import com.example.bckndApi.data.User;
import com.example.bckndApi.repository.UserRepository;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(maxAge=3600)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("users/create")
    public ResponseEntity<?> createUser(@RequestBody User user){
        var hashedPass = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPass);
        System.out.println(user); // Log para verificar qué datos están llegando
        userRepository.save(user);
        return ResponseEntity.status(200).body(user);
    }
    // user/list
    //user/delete

    @GetMapping("users/list")
    public ResponseEntity<?> listUser(@RequestHeader("Authorization") String authorization){
        if(authorization.startsWith("Bearer ")){
            var token = authorization.substring(7);
            try{
                JwtUtil.validateToken(token);
                var users=userRepository.findAll();
                return ResponseEntity.status(200).body(users);
            } catch(Exception ex){
                ex.printStackTrace();
                return ResponseEntity.status(401).body(ex.getMessage());
            }
        } else{
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));}
}

    @PostMapping("users/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        var hashedPass = PasswordHasher.hashPassword(user.getPassword());
        var optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), hashedPass);
        if(optionalUser.isPresent()){
            var token = JwtUtil.generateToken(user.getEmail());
            return ResponseEntity.status(200).body(Map.of(
                    "access_token",token
            ));
        } else{
            return ResponseEntity.status(404).body(Map.of(
                    "message","El usuario con ese mail o password no existe"
            ));
        }
    }

    @GetMapping("users/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            try {
                String email = JwtUtil.extractEmailFromToken(token);
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