package com.ciandt.ExceptionsMyMusic.domain.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoContentException(String message) {
        super(message);
    }
}
