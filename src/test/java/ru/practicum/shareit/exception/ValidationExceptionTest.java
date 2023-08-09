package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ValidationExceptionTest {
    @Test
    public void shouldValidationExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидациии.",
                "Ошибка валидациии.");
        ValidationException exception = new ValidationException(
                "Ошибка валидациии.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
