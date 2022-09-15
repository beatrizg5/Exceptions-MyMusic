package com.ciandt.ExceptionsMyMusic.application.controllers;

import com.ciandt.ExceptionsMyMusic.application.client.MyFeignClient;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(UserController.class);
    @Autowired
    private MyFeignClient myFeignClient;

    @Autowired
    private UserService userService;

    @PostMapping
    public String createUserIdClient(@RequestBody TokenDataDTO tokenDataDTO) {
        return myFeignClient.clientUserId(tokenDataDTO);
    }

    @Operation(summary = "Find User By Id", description = "Receive an Id from User and show the information about it")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Operation"),
            @ApiResponse(code = 400, message = "User Not Found"),
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String userId) {
        UserDTO userDTO = userService.findUserById(userId);

        LOGGER.info("Operation performed successfully!");
        return ResponseEntity.ok().body(userDTO);
    }
}