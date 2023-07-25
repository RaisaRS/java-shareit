package ru.practicum.shareit.exceptions;

public class InCorrectDateException extends RuntimeException {
    public InCorrectDateException(String message) {
        super(message);
    }
}
