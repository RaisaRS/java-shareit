package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item saveItem(ItemDto itemDto, long userId);

    Item updateItem(ItemDto itemDto, long userId);

    ItemDto getItemById(long userId, Long itemId);

    List<ItemDto> getItemsByUser(long userId);

    Collection<Item> searchItem(String text);

    CommentDto postComment(long userId, Long itemId, CommentDto commentDto);

    void deleteItemById(long userId, Long itemId);
}
