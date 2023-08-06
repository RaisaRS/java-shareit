package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.ItemUnavailableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ItemUnavailableExceptionTest {
    @Test
    public void shouldItemUnavailableExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Аренда предмета недоступна.",
                "Аренда предмета недоступна.");
        ItemUnavailableException exception = new ItemUnavailableException(
                "Аренда предмета недоступна.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
