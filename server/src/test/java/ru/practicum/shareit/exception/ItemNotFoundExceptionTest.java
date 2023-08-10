package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.ItemNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ItemNotFoundExceptionTest {
    @Test
    public void shouldItemNotFoundExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Предмет не найден.",
                "Предмет не найден.");
        ItemNotFoundException exception = new ItemNotFoundException(
                "Предмет не найден.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
