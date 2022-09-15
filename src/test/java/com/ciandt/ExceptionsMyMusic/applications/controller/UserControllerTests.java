package com.ciandt.ExceptionsMyMusic.applications.controller;

import com.ciandt.ExceptionsMyMusic.application.client.MyFeignClient;
import com.ciandt.ExceptionsMyMusic.application.controllers.UserController;
import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.*;
import com.ciandt.ExceptionsMyMusic.domain.services.TokenService;
import com.ciandt.ExceptionsMyMusic.domain.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static java.nio.file.Paths.get;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyFeignClient myFeignClient;

    @InjectMocks
    private UserController userController;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;
    private TokenDataDTO tokenDataDTO;
    private UserType userType;
    private User user;
    private Playlist playlist;
    private String userExistingId;
    private String userNotExistsId;
    private Data userNameDTO;
    private Set<Music> musicList =  new HashSet<>();
    private Set<User> usersList = new HashSet<>();



    @BeforeEach
    void setUp() throws Exception {
        userNameDTO = new Data("ana", "");
        playlist = new Playlist("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", musicList, user);
        user = new User("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", "ana", userType, playlist);
        userType = new UserType("ed7f6acb-1aad-42c9-8c7b-5a49540fcbc4", "Comum", usersList);
        userDTO = new UserDTO("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", "ana", playlist, userType);

    }

    @Test
    public void shouldGenerateToken() throws Exception {

        Mockito.when(tokenService.generateToken(any(TokenDataDTO.class))).thenReturn("ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==");
        String json = objectMapper.writeValueAsString(userNameDTO);
        ResultActions result = mockMvc.perform(post("/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
                result.andExpect(status().isOk());
    }

    @Test
    public void shouldFindUserByIdPassingThePathVariable() throws Exception {
        Mockito.when(userService.findUserById(userExistingId)).thenReturn(userDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}