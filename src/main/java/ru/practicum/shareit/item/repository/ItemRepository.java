package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository {
    Item saveItem(Item item);

    Item updateItem(long ownerId, Item item);

    Item getItemById(Long itemId);

    Collection<Item> getAllItems();

    List<Item> getAllItemsByUser(long userId);

    Collection<Item> searchItem(String text);

    void deleteItemById(Long itemId);
}
