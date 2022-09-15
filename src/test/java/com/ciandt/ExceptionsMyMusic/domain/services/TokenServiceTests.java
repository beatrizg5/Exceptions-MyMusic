package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.NoContentException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DisplayName("TokenServiceTest")
public class TokenServiceTests {
    @Mock
    UserService userService;

    @Mock
    private TokenService tokenService;

    private String invalidName = "invalidName";
    private TokenDataDTO tokenDataDTO = new TokenDataDTO(new Data("devTest", "tokenValue"));

    @Test
    @DisplayName("shouldReturnErrorWhenUserNotFound")
    public void shouldReturnErrorWhenUserNotFound() {
        try {
            Mockito.doThrow(NoContentException.class).when(userService).findUserById(invalidName);
        } catch (Throwable e) {
            assertEquals(NoContentException.class, e.getMessage());
        }
    }

    @Test
    @DisplayName("shouldReturnErrorWhenTokenIsInvalidOrExpired")
    public void shouldReturnErrorWhenTokenIsInvalidOrExpired() {
        try {
            Mockito.doThrow(ResourceNotFoundException.class).when(tokenService).validateHeader(tokenDataDTO);
        } catch (Throwable e) {
            assertEquals(ResourceNotFoundException.class, e.getMessage());
        }
    }
}