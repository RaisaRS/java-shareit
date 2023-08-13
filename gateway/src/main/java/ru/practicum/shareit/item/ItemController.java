package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> saveItem(@NotNull @Validated(Validation.Post.class) @RequestBody ItemDto itemDto,
                                           @RequestHeader(name = USER_ID_HEADER)
                                           @Min(value = 1, message = "User ID must be more than 0") Long userId) {
        log.info("Получен POST-запрос /items {} ", itemDto);
        ResponseEntity<Object> response = itemClient.saveItem(itemDto, userId);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(name = USER_ID_HEADER)
                                             @Min(value = 1, message = "User ID must be more than 0") Long userId,
                                             @NotNull @Validated(Validation.Patch.class) @RequestBody ItemDto itemDto,
                                             @PathVariable @Min(value = 1, message = "Item ID must be more than 0")
                                             Long itemId) {
        log.info("Получен PATCH-запрос /itemId {} ", itemId);
        itemDto.setId(itemId);
        ResponseEntity<Object> response = itemClient.updateItem(itemDto, userId);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(name = USER_ID_HEADER, required = false)
                                              @Min(value = 1, message = "User ID must be more than 0") Long userId,
                                              @PathVariable @Min(value = 1, message = "Item ID must be more than 0")
                                              Long itemId) {
        log.info("Получен GET-запрос /itemId {} ", itemId);
        ResponseEntity<Object> response = itemClient.getItemById(userId, itemId);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader(name = USER_ID_HEADER, required = false)
                                                     @Min(value = 0, message = "UserId must be more than 0")
                                                     Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0")
                                                 @Min(value = 0, message = "From must be more than 0") Integer from,
                                                 @RequestParam(name = "size", defaultValue = "20")
                                                     @Min(value = 0, message = "Size must be more than 0")
                                                     Integer size) {
        log.info("Получен GET-запрос: список всех предметов одного пользователя {} ", userId);
        ResponseEntity<Object> response = itemClient.getItemByUser(userId, from, size);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(name = USER_ID_HEADER)
                                             @Min(value = 1, message = "User ID must be more than 0") Long userId,
                                             @RequestParam String text,
                                             @RequestParam(name = "from", defaultValue = "0") @Min(value = 0,
                                                     message = "Parameter 'from' must be more than 0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "20") @Min(value = 0,
                                                     message = "Parameter 'size' must be more than 0") Integer size) {
        log.info("Получен GET-запрос /text {} , от ID пользователя: {} ", text, userId);
        ResponseEntity<Object> response = itemClient.searchItem(userId, text, from, size);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User ID must be more than 0") Long userId,
                                              @PathVariable @Min(value = 1, message = "Item ID must be more than 0")
                                              Long itemId,
                                              @RequestBody @Validated CommentDto commentDto) {
        log.info("Получен POST-запрос: добавление отзыва {} о бронировании ID предмета {} от ID пользователя {}",
                commentDto, itemId, userId);
        ResponseEntity<Object> response = itemClient.postComment(userId, itemId, commentDto);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = USER_ID_HEADER) @Min(value = 1,
            message = "User ID must be more than 0") Long userId,
                           @PathVariable @Min(value = 1, message = "Item ID must be more than 0") Long itemId) {
        log.info("Получен DELETE- запрос на уаление предмета, ID: {}, ID от пользователя {}", itemId, userId);
        ResponseEntity<Object> response = itemClient.deleteItem(userId, itemId);
        log.info("Ответ на запрос: {}", response);
    }
}
