package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item saveItem(Item item) {
        if (item.getAvailable() == null)
            throw new ValidationException("Ошибка валидации" + item.getAvailable());
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long ownerId, Item item) {
        Item updateItem = items.get(item.getId());
        if (!updateItem.getOwnerId().equals(ownerId)) {
            throw new UserNotFoundException("Владелец вещи не найден " + ownerId);
        }

        if (Objects.nonNull(item.getName())) {
            updateItem.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription())) {
            updateItem.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable())) {
            updateItem.setAvailable(item.getAvailable());
        }
        return updateItem;
    }


    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItem(String text) {
        return items.values().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(Long itemId) {
        items.remove(itemId);
    }
}
