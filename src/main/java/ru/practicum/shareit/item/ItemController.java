package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
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
                            @RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("Получен POST-запрос /items {} ", itemDto);
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                              @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        log.info("Получен PATCH-запрос /itemId {} ", itemId);
        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Получен GET-запрос /itemId {} ", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("Получен GET-запрос: список всех предметов одного пользователя {} ", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam @NotBlank String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        log.info("Получен GET-запрос /text {} ", text);
        return itemService.searchItem(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = USER_ID_HEADER) long userId,
                           @PathVariable long itemId) {
        log.info("Получен DELETE- запрос на уаление вещи {} ", itemId);
        itemService.deleteItemById(userId, itemId);
    }

    //Вот основные сценарии, которые должно поддерживать приложение.
    /*Добавление новой вещи. Будет происходить по эндпойнту POST /items.
     На вход поступает объект ItemDto. userId в заголовке X-Sharer-User-Id — это идентификатор пользователя,
     который добавляет вещь. Именно этот пользователь — владелец вещи.
     Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.
     */

    /*Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
    Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
     */
    /*Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
     Информацию о вещи может просмотреть любой пользователь.
     */
    /*Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.

     */
    /*Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст,
     и система ищет вещи, содержащие этот текст в названии или описании.
     Происходит по эндпойнту /items/search?text={text},
      в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.
     */

    /*Для каждого из данных сценариев создайте соответственный метод в контроллере.
    Также создайте интерфейс ItemService и реализующий его класс ItemServiceImpl,
    к которому будет обращаться ваш контроллер. В качестве DAO создайте реализации,
    которые будут хранить данные в памяти приложения. Работу с базой данных вы реализуете в следующем спринте.
     */
}
