package com.example.bckndApi.controller;

import com.example.bckndApi.data.User;
import com.example.bckndApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge=3600)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("users/create")
    public ResponseEntity<?> createUser(@RequestBody User user){
        System.out.println(user); // Log para verificar qué datos están llegando
        userRepository.save(user);
        return ResponseEntity.status(200).body(user);
    }
    // user/list
    //user/delete

    @GetMapping("users/list")
    public ResponseEntity<?> listUser(){
        var users=userRepository.findAll();
        return ResponseEntity.status(200).body(users);
    }


}
