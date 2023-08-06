package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemDtoReqTest {
    @Test
    public void createItemDtoTest() {
        ItemDtoReq itemDtoReq = ItemDtoReq.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Уничтожает пыль и грязь")
                .ownerId(1L)
                .available(true)
                .requestId(1L)
                .build();

        assertNotNull(itemDtoReq);
        assertEquals("Робот-пылесос", itemDtoReq.getName());
        assertEquals("Уничтожает пыль и грязь", itemDtoReq.getDescription());
        assertEquals(1L, itemDtoReq.getOwnerId());
        assertEquals(true, itemDtoReq.getAvailable());
        assertEquals(1L, itemDtoReq.getRequestId());

    }

    @Test
    public void testEquals() {
        ItemDtoReq item1 = ItemDtoReq.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .build();

        ItemDtoReq item2 = ItemDtoReq.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .build();

        ItemDtoReq item3 = ItemDtoReq.builder()
                .id(2L)
                .name("Another item")
                .description("Another description")
                .build();

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    @Test
    public void testHashCode() {
        ItemDtoReq item1 = ItemDtoReq.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .build();

        ItemDtoReq item2 = ItemDtoReq.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .build();

        assertEquals(item1.hashCode(), item2.hashCode());
    }
}
