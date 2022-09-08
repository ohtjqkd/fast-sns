package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.model.User;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import com.fastcampus.fastsns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private String expiredTimeMs;

    @Transactional
    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new FastSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new FastSnsApplicationException(ErrorCode.USER_NOT_FOUND, ""));

        if(!encoder.matches(password, userEntity.getPassword())) {
            throw new FastSnsApplicationException(ErrorCode.INVALID_PASSWORD, "");
        }

        String token = JwtTokenUtils.generateToken(userName, secretKey, Long.parseLong(expiredTimeMs));
        return token;
    }

    public User loadUserByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                new FastSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
