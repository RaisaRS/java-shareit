package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long userId);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsByUser(long userId);

    Collection<ItemDto> searchItem(String text);

    void deleteItemById(long userId, Long itemId);
}
