package com.fastcampus.fastsns.repository;

import com.fastcampus.fastsns.model.Comment;
import com.fastcampus.fastsns.model.entity.CommentEntity;
import com.fastcampus.fastsns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
