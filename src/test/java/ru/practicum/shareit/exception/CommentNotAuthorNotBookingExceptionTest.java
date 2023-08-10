package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exceptions.CommentNotAuthorNotBookingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentNotAuthorNotBookingExceptionTest {
    @Test
    public void shouldCommentNotAuthorNotBookingExceptionTest() {
        ErrorResponse errorResponse = new ErrorResponse("Комментарии доступны только пользователям," +
                " ранее бронировавшим предмет.",
                "Комментарии доступны только пользователям, ранее бронировавшим предмет.");
        CommentNotAuthorNotBookingException exception = new CommentNotAuthorNotBookingException(
                "Комментарии доступны только пользователям, ранее бронировавшим предмет.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());

    }
}
