package com.example.bckndApi.repository;

import com.example.bckndApi.data.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
