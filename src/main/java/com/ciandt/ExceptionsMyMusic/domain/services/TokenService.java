package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.client.MyFeignClient;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class TokenService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private MyFeignClient myFeignClient;

    @Autowired
    private UserService userService;

    public String generateToken(TokenDataDTO userIdDTO) {
        if (userService.findUserById(userIdDTO.getData().getName()) == null) {
            LOGGER.error("Invalid user!");
            throw new ResourceNotFoundException("Invalid user");
        }
        return myFeignClient.clientUserId(userIdDTO);
    }

    public void validateHeader(TokenDataDTO tokenDataDTO) {
        try {
            UserDTO userDTO = userService.findUserById(tokenDataDTO.getData().getName());
            if (!myFeignClient.clientValidator(tokenDataDTO).equals("ok") && userDTO != null) {
                throw new ResourceNotFoundException("Invalid or expired token");
            }
        } catch (FeignException e) {
            LOGGER.error("Invalid or expired token! Generated a new token and try again");
            throw new ResourceNotFoundException("Invalid or expired token");
        }
    }
}