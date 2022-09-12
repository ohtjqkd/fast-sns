package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.fixture.PostEntityFixture;
import com.fastcampus.fastsns.fixture.UserEntityFixture;
import com.fastcampus.fastsns.model.entity.PostEntity;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.PostEntityRepository;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
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
    @Test
    public void 포스트수정_성공() throws Exception {
        //given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(postEntityFixture.getUser()));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntityFixture));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntityFixture);

        //then
        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));
    }
    @Test
    public void 포스트수정_포스트존재x() throws Exception {
        //given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(postEntityFixture.getUser()));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        //then
        FastSnsApplicationException e = Assertions.assertThrows(FastSnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트수정_권한x() throws Exception {
        //given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 2);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntityFixture));

        //then
        FastSnsApplicationException e = Assertions.assertThrows(FastSnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
    @Test
    public void 포스트삭제_성공() throws Exception {
        //given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(postEntityFixture.getUser()));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntityFixture));

        //then
        Assertions.assertDoesNotThrow(() -> postService.delete(userName, postId));
    }
    @Test
    public void 포스트삭제_포스트존재x() throws Exception {
        //given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(postEntityFixture.getUser()));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        //then
        FastSnsApplicationException e = Assertions.assertThrows(FastSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트삭제_권한x() throws Exception {
        //given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntityFixture = PostEntityFixture.get(userName, postId, 2);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntityFixture));

        //then
        FastSnsApplicationException e = Assertions.assertThrows(FastSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    public void 피드목록() {

        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    public void 내피드목록() {
        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.my("", pageable));
    }
}
