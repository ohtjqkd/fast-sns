package com.fastcampus.fastsns.repository;

import com.fastcampus.fastsns.model.entity.LikeEntity;
import com.fastcampus.fastsns.model.entity.PostEntity;
import com.fastcampus.fastsns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity userEntity, PostEntity postEntity);

//    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post =:post")
    Integer countByPost(PostEntity post);

    List<LikeEntity> findByPost(PostEntity postEntity);
}
