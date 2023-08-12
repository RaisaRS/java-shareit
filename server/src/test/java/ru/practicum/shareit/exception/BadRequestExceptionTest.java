package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BadRequestExceptionTest {
    @Test
    public void shouldBadRequestExceptionTest() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Bad Request";

        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(httpStatus, message);

        assertEquals(httpStatus, methodArgumentNotValidException.getStatus());
        assertEquals(message, methodArgumentNotValidException.getReason());
    }

    @Test
    public void testBadRequestExceptionWithCustomHttpStatus() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Custom HTTP Status
        String message = "Internal Server Error";

        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(httpStatus, message);

        assertEquals(httpStatus, methodArgumentNotValidException.getStatus());
        assertEquals(message, methodArgumentNotValidException.getReason());
    }
}
