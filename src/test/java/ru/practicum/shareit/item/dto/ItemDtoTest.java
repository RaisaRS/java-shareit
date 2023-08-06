package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemDtoTest {
    @Test
    public void testEquals() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Уничтожает пыль и грязь")
                .ownerId(1L)
                .available(true)
                .comments(new ArrayList<>())
                .lastBooking(new BookingDtoShort())
                .nextBooking(new BookingDtoShort())
                .requestId(1L)
                .build();

        ItemDto itemDto1 = ItemDto.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Уничтожает пыль и грязь")
                .ownerId(1L)
                .available(true)
                .comments(new ArrayList<>())
                .lastBooking(new BookingDtoShort())
                .nextBooking(new BookingDtoShort())
                .requestId(1L)
                .build();

        assertEquals(itemDto, itemDto1);
    }


    @Test
    public void testHashCode() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Уничтожает пыль и грязь")
                .ownerId(1L)
                .available(true)
                .comments(new ArrayList<>())
                .lastBooking(new BookingDtoShort())
                .nextBooking(new BookingDtoShort())
                .requestId(1L)
                .build();

        ItemDto itemDto1 = ItemDto.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Уничтожает пыль и грязь")
                .ownerId(1L)
                .available(true)
                .comments(new ArrayList<>())
                .lastBooking(new BookingDtoShort())
                .nextBooking(new BookingDtoShort())
                .requestId(1L)
                .build();


        assertEquals(itemDto.hashCode(), itemDto1.hashCode());
    }
}
