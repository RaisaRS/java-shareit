package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.mappers.ItemMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto saveItem(@RequestBody @Valid ItemDto itemDto,
                            @RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("Получен POST-запрос /items {} ", itemDto);
        return ItemMapper.toItemDto(itemService.saveItem(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                              @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        log.info("Получен PATCH-запрос /itemId {} ", itemId);
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemService.updateItem(itemDto, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                               @PathVariable Long itemId) {
        log.info("Получен GET-запрос /itemId {} ", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "20") int size) {
        log.info("Получен GET-запрос: список всех предметов одного пользователя {} ", userId);
        return itemService.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam @NotBlank String text,
                                          @RequestParam(name = "from", defaultValue = "0") int from,
                                          @RequestParam(name = "size", defaultValue = "20") int size) {
        log.info("Получен GET-запрос /text {} ", text);
        return itemService.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                  @PathVariable Long itemId,
                                  @RequestBody CommentDto commentDto) {
        log.info("Получен POST-запрос: добавление отзыва о бронировании предмета {} ", itemId);
        return itemService.postComment(userId, itemId, commentDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                           @PathVariable long itemId) {
        log.info("Получен DELETE- запрос на уаление предмета {} ", itemId);
        itemService.deleteItemById(userId, itemId);
    }
}
