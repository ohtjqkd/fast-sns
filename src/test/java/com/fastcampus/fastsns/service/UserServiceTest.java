package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.FastSnsApplication;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.fixture.UserEntityFixture;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입_정상작동() throws Exception {
        //given
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password, 1)));

        //then
        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }
    @Test
    void 회원가입_이미_가입한_경우() throws Exception {
        //given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);
        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        //then
        Assertions.assertThrows(FastSnsApplicationException.class, () -> userService.join(userName, password));
    }

    @Test
    void 로그인_정상작동() throws Exception {
        //given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        //then
        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }
    @Test
    void 로그인_유저가_x() throws Exception {
        //given
        String userName = "userName";
        String password = "password";

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(FastSnsApplicationException.class, () -> userService.login(userName, password));
    }
    @Test
    void 로그인_패스워드_오류() throws Exception {
        //given
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        //then
        Assertions.assertThrows(FastSnsApplicationException.class, () -> userService.login(userName, wrongPassword));
    }
}
