package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.BookingNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingNotFoundExceptionTest {
    @Test
    public void shouldBookingNotFoundExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Бронирование не найдено.",
                "Бронирование не найдено.");
        BookingNotFoundException bookingNotFoundException = new BookingNotFoundException("Бронирование не найдено.");

        assertNotNull(errorResponse);
        assertNotNull(bookingNotFoundException);
        assertEquals(errorResponse.getDescription(), bookingNotFoundException.getMessage());
        assertEquals(errorResponse.getError(), bookingNotFoundException.getMessage());
    }
}
