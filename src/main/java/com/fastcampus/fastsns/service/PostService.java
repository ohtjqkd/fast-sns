package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.model.entity.PostEntity;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.PostEntityRepository;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public PostEntity create(String title, String body, String userName) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new FastSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));
        return saved;
    }
}
