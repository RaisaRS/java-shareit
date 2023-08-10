package ru.practicum.shareit.exceptions;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
