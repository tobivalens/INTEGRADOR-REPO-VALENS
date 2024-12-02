package com.example.bckndApi.controller;

import com.example.bckndApi.data.*;
import com.example.bckndApi.repository.CommentRepository;
import com.example.bckndApi.repository.DoctorRepository;
import com.example.bckndApi.repository.MeasureRepository;
import com.example.bckndApi.util.JwtUtil;
import com.example.bckndApi.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/addComment")
    public ResponseEntity<?> addCommentary(@RequestHeader("Authorization") String authorization, @RequestBody CommentInfo info) {
        if (validateToken(authorization)) {
            String contenido = info.getContent();
            Optional<Measure> optionalMeasure = measureRepository.findById(info.getMeasureId());
            Optional<Doctor> optionalDoctor = doctorRepository.findById(info.getDoctorId());

            Comment comment = new Comment();
            if (contenido != null) {
                comment.setContent(contenido);
            }
            if (optionalMeasure.isPresent() && optionalDoctor.isPresent()) {
                Measure measure = optionalMeasure.get();
                comment.setMeasure(measure);

                Doctor doctor = optionalDoctor.get();
                comment.setDoctor(doctor);

                commentRepository.save(comment);
                return new ResponseEntity<>(comment, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @Transactional
    @DeleteMapping("/deleteComments/{id}")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        if (validateToken(authorization)) {
            Optional<Comment> comment = commentRepository.findById(id);

            if (comment.isPresent()) {
                commentRepository.deleteById(id);
                return ResponseEntity.ok().body("Comentario eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentario no encontrado");
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @GetMapping("comments/measureCommentary/{id}")
    public ResponseEntity<?> measureCommentary(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long measureId) {
        if (validateToken(authorization)) {
            Optional<Measure> optionalMeasure = measureRepository.findById(measureId);

            if (optionalMeasure.isPresent()) {
                List<Comment> comments = commentRepository.findByMeasureId(measureId);

                if (comments.isEmpty()) {
                    return ResponseEntity.status(404).body(Map.of("message", "No se encontraron comentarios para esta medición"));
                }

                return ResponseEntity.status(200).body(comments);
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Medición no encontrada"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    @GetMapping("comments/doctorComment/{id}")
    public ResponseEntity<?> doctorComment(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long commentId) {
        if (validateToken(authorization)) {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                Doctor doctor = optionalComment.get().getDoctor();
                return ResponseEntity.status(200).body(doctor.getName());
            }
            return ResponseEntity.status(400).body("No se encontró el comentario");
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
    }

    // Método para validar el token
    private boolean validateToken(String authorization) {
        if (authorization.startsWith("Bearer ")){
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
}

