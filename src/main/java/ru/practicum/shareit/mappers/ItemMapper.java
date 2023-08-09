package ru.practicum.shareit.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();
    }

    public static Item toItem(User user, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwnerId(user.getId());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDtoReq toItemDtoReq(Item item) {
        return ItemDtoReq.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequest())
                .available(item.getAvailable())
                .build();
    }

    public static List<ItemDtoReq> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDtoReq)
                .collect(Collectors.toList());
    }
}
