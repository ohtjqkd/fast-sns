package com.fastcampus.fastsns.repository;

import com.fastcampus.fastsns.model.Post;
import com.fastcampus.fastsns.model.entity.PostEntity;
import com.fastcampus.fastsns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    public Page<Post> findAllByUser(UserEntity userEntity, Pageable pageable);
}
