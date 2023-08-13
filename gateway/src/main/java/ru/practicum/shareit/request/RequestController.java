package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.constraints.Min;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User id should be more than 0")Long userId,
                                                 @Validated @RequestBody(required = false) RequestDto requestDto) {
        log.info("Получен POST-запрос /requests {} ", requestDto);
        ResponseEntity<Object> response = requestClient.addItemRequest(userId, requestDto);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User id should be more than 0") Long userId,
                                                   @RequestParam(defaultValue = "0") @Min(value = 0,
                                                           message = "Parameter 'from' must be more than 0")
                                                   Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(value = 0,
                                                           message = "Parameter 'size' must be more than 0")
                                                  Integer size) {
        log.info("Получен GET-запрос от ID пользователя {} на получение списка своих запросов вместе с данными о них." +
                " Результаты возвращаются постранично от {}, в количестве {}.", userId, from, size);
        ResponseEntity<Object> response = requestClient.requestsGet(userId, from, size);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User id should be more than 0") Long userId,
                                                      @RequestParam(name = "from", defaultValue = "0") @Min(value = 0,
                                                              message = "Parameter 'from' must be more than 0")
                                                      Integer from,
                                                      @RequestParam(name = "size", defaultValue = "20") @Min(value = 0,
                                                              message = "Parameter 'size' must be more than 0")
                                                     Integer size) {
        log.info("Получен GET-запрос от ID польбзователя {} на получение списка запросов, " +
                "созданных другимим пользователями. " +
                "Результаты возвращаются постранично от {} в количестве {}.", userId, from, size);
        ResponseEntity<Object> response = requestClient.getRequestsAll(userId, from, size);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User id should be more than 0") Long userId,
                                                @PathVariable @Min(value = 1,
                                                        message = "Request id should be more than 0")  Long requestId) {
        log.info("Получен GET-запрос от ID пользователя {} " +
                "на получение данных об одном конкретном запросе ID: {} с данными об ответах.", userId, requestId);
        ResponseEntity<Object> response = requestClient.getRequestById(userId, requestId);
        log.info("Ответ на запрос: {}", response);
        return response;
    }
}
