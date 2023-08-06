package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingDtoItemTest {
    @Test
    public void createBookingDtoItemTest() {
        BookingDtoItem bookingDtoItem = BookingDtoItem.builder()
                .id(1L)
                .name("name")
                .build();

        assertNotNull(bookingDtoItem);
        assertEquals(1L, bookingDtoItem.getId());
        assertEquals("name", bookingDtoItem.getName());
    }
}
