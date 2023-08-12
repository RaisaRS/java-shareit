package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MethodArgumentNotValidException extends ResponseStatusException {
    public MethodArgumentNotValidException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}