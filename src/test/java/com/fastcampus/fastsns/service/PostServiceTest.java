package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.model.entity.PostEntity;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.PostEntityRepository;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    public void 포스트작성성공() throws Exception {
        //given
        String title = "title";
        String body = "body";
        String userName = "userName";
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        //then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    public void 포스트작성시_요청한유저가x() throws Exception {
        //given
        String title = "title";
        String body = "body";
        String userName = "userName";
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        //then
        FastSnsApplicationException e = Assertions.assertThrows(FastSnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}
