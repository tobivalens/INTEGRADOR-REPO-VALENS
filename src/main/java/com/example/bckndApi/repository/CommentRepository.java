package com.example.bckndApi.repository;

import com.example.bckndApi.data.Comment;
import com.example.bckndApi.data.Measure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByMeasureId(Long  id);
}
