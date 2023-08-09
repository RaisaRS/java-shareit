package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.RequestNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RequestNotFoundExceptionTest {
    @Test
    public void constructorTest() {
        String expectedMessage = "Not found";
        RequestNotFoundException exception = new RequestNotFoundException(HttpStatus.NOT_FOUND, expectedMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void constructorWithCustomStatusAndMessageTest() {
        String expectedMessage = "Custom message";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        RequestNotFoundException exception = new RequestNotFoundException(expectedStatus, expectedMessage);
        assertEquals(expectedStatus, exception.getStatus());

    }
}
