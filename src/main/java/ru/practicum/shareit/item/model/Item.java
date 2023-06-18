package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.practicum.shareit.request.ItemRequest;


/**
 * TODO Sprint add-controllers.
 */
@Data
@Getter
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
    private ItemRequest request;
}
