package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, long userId) throws ValidationException {
        User user = userRepository.getUserById(userId);
        Item item = toItem(itemDto);
        item.setOwnerId(user.getId());
        log.info("Добавлен предмет {}, владелец: id = {}", itemDto, userId);
        return toItemDto(itemRepository.saveItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);
        Item item = toItem(itemDto);
        item.setOwnerId(userId);
        log.info("Выполнено обновление информации о предмете = {}, " +
                "принадлежащем пользователю, id = {}", itemDto, userId);
        return toItemDto(itemRepository.updateItem(userId, item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        log.info("Получен предмет, id = {}", itemId);
        return toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUser(long userId) {
        userRepository.getUserById(userId);
        log.info("Список всех предметов, принадлежащих пользователю, id = {}", userId);
        return itemRepository.getAllItemsByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        log.info("Выполнен поиск среди предметов по : {}.", text);
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long userId, Long itemId) {
        userRepository.getUserById(userId);
        itemRepository.deleteItemById(itemId);
        log.info("Удален предмет {}, принадлежащий пользователю {}", itemId, userId);
    }
}
