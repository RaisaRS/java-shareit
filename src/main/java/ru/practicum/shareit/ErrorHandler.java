package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.error("Получен статус 409  {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Пользователь с таким email уже существует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Получен статус 400  {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Ошибка валидациии");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInСorrectBookingException(final InCorrectBookingException e) {
        log.error("Получен статус 400  {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Ошибка бронирования");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInCorrectDateException(final InCorrectDateException e) {
        log.error("Получен статус 400  {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Некорректная дата бронирования");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInCorrectStatusException(final InCorrectStatusException e) {
        log.error("Получен статус 400  {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Некорректный статус бронирования");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Пользователь не найден.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Вещь не найдена.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Бронирование не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final RuntimeException e) {
        log.error("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Сервер прилёг отдохнуть. Но обещал вернуться;)");
    }
}
