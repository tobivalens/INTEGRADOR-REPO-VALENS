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
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Método para editar un usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable("id") Long id, @RequestBody User usuarioActualizado) {
        var usuario = userRepository.findById(id);
        if (usuario.isPresent()) {
            User usuarioExistente = usuario.get();
            usuarioExistente.setName(usuarioActualizado.getName());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
            usuarioExistente.setLastname(usuarioActualizado.getLastname());
            usuarioExistente.setCedula(usuarioActualizado.getCedula());
            usuarioExistente.setDisease(usuarioActualizado.getDisease());
            usuarioExistente.setState(usuarioActualizado.getState());
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
            userRepository.save(usuarioExistente);
            return ResponseEntity.status(200).body(usuarioExistente);
        } else {
            var response = new HashMap<String, String>();
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Método para eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable("id") Long id) {
        var usuario = userRepository.findById(id);
        if (usuario.isPresent()) {
            userRepository.delete(usuario.get());
            var response = new HashMap<String, String>();
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.status(200).body(response);
        } else {
            var response = new HashMap<String, String>();
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }
}


}
