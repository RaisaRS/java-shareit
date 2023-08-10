package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.UnsupportedStateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UnsupportedStateExceptionTest {
    @Test
    public void shouldUnsupportedStateExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Неверный статус бронирования.",
                "Неверный статус бронирования.");
        UnsupportedStateException exception = new UnsupportedStateException(
                "Неверный статус бронирования.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
