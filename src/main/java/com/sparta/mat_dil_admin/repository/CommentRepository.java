package com.sparta.mat_dil_admin.repository;

import com.sparta.mat_dil_admin.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
