package com.ciandt.ExceptionsMyMusic.domain.services.exceptions;

import com.ciandt.ExceptionsMyMusic.application.controllers.exceptions.StandardError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> UserNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status;

        if (e.getMessage().equals("User not found"))
            status = HttpStatus.UNAUTHORIZED;
        else
            status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Invalid request");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> UserNotAllowed(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status;

        if (e.getMessage().equals("You have reached the maximum number of songs in your playlist. To add more songs, subscribe to the premium plan."))
            status = HttpStatus.UNAUTHORIZED;
        else
            status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Invalid request");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> playlistNotHaveUser(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status;

        if (e.getMessage().equals("This playlist does not belongs to this user"))
            status = HttpStatus.UNAUTHORIZED;
        else
            status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Invalid request");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
