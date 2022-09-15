package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.repositories.UserRepository;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.User;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.NoContentException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDTO findUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            LOGGER.error("User not found on database");
            throw new ResourceNotFoundException("User not found");
        }
        UserDTO userDTO = new UserDTO(optionalUser.get());

        LOGGER.info("Successfully search, user found in database");
        return userDTO;
    }
}
