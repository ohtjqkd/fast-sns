package com.fastcampus.fastsns.controller;

import com.fastcampus.fastsns.controller.request.UserJoinRequest;
import com.fastcampus.fastsns.controller.request.UserLoginRequest;
import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.fixture.UserEntityFixture;
import com.fastcampus.fastsns.model.User;
import com.fastcampus.fastsns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        //given
        String userName = "userName";
        String password = "password";

        when(userService.join(userName, password)).thenReturn(mock(User.class));
        mockMvc.perform(post("/api/v1/users/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
        //when

        //then
    }

    @Test

    public void 회원가입_이미_회원가입() throws Exception {
        //given
        String userName = "userName";
        String password = "password";

        when(userService.join(userName, password)).thenThrow(new FastSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isConflict());
        //when

        //then
    }

    @Test
    public void 로그인() throws Exception {

        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인_유저가_존재_x() throws Exception {

        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenThrow(new FastSnsApplicationException(ErrorCode.USER_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인_비밀번호_오류() throws Exception {

        String userName = "userName";
        String password = "password";
        when(userService.login(userName, password)).thenThrow(new FastSnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 알람기능() throws Exception {
        //given
        when(userService.alarmList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("/api/v1/users/alarm")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //when

        //then
    }

    @Test
    @WithAnonymousUser
    public void 알람기능_로그인x() throws Exception {
        //given
        when(userService.alarmList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("/api/v1/users/alarm")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
