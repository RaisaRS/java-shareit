package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, long userId) throws ValidationException {
        User user = userRepository.getUserById(userId);
        Item item = toItem(itemDto);
        item.setOwnerId(user.getId());
        return toItemDto(itemRepository.saveItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);
        Item item = toItem(itemDto);
        item.setOwnerId(userId);
        return toItemDto(itemRepository.updateItem(userId, item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUser(long userId) {
        userRepository.getUserById(userId);
        return itemRepository.getAllItemsByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long userId, Long itemId) {
        userRepository.getUserById(userId);
        itemRepository.deleteItemById(itemId);
    }
}
