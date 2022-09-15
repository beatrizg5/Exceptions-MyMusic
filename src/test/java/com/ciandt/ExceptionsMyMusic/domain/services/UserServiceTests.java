package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.repositories.UserRepository;
import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.*;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.DatabaseException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;
    private Data userNameDTO;
    private String userExistsId;
    private String userNoExistsId;
    private TokenDataDTO tokenDataDTO;
    private Playlist playlist;
    private UserDTO userDTO;
    private UserType userType;
    private User user;
    private Set<Music> musicList =  new HashSet<>();
    private Set<User> usersList = new HashSet<>();



    @BeforeEach
    void setUp() throws Exception {
        userNameDTO = new Data("ana",
                "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==");
        userExistsId = "74bfbda5-c825-40a8-b9ac-67b2b32729ec";
        userNoExistsId = "74bfbda5-c825-40a8-b9ac-67b2b";
        playlist = new Playlist("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", musicList, user);
        user = new User("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", "ana", userType, playlist);
        userType = new UserType("ed7f6acb-1aad-42c9-8c7b-5a49540fcbc4", "Comum", usersList);
        userDTO = new UserDTO("0c2a04a5-d8d2-42a2-a90f-3d6e8f912b88", "ana", playlist, userType);
        Mockito.doNothing().when(tokenService).validateHeader(tokenDataDTO);
    }

    @Test
    void shouldReturn401WhenTokenIsNotValid() {
        Mockito.doThrow(DatabaseException.class).when(tokenService).validateHeader(tokenDataDTO);
        Mockito.when(userRepository.getById(userExistsId)).thenReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserById(userExistsId);
        });
    }

    @Test
    void shouldReturn400WhenUserNotExist() {
        Mockito.when(userRepository.getById(userNoExistsId)).thenThrow(ResourceNotFoundException.class);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserById(userNoExistsId);
        });
    }

    @Test
    void shouldReturn200WhenUserExists() {
        Mockito.when(userRepository.findById(userExistsId)).thenReturn(Optional.ofNullable(user));
        userService.findUserById(userExistsId);
        Assertions.assertEquals("74bfbda5-c825-40a8-b9ac-67b2b32729ec",userExistsId);
    }
}

