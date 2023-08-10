package ru.practicum.shareit.exceptions;

public class CommentNotAuthorNotBookingException extends RuntimeException {
    public CommentNotAuthorNotBookingException(String message) {
        super(message);
    }
}
