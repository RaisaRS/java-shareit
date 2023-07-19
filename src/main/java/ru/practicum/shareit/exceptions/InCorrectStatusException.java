package ru.practicum.shareit.exceptions;

public class InCorrectStatusException extends RuntimeException {
    public InCorrectStatusException(String message) {
        super(message);
    }
}
