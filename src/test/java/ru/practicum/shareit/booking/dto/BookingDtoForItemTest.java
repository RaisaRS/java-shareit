package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingDtoForItemTest {
    @Test
    public void bookingDtoForItemTest() {
        BookingDtoForItem bookingDtoForItem = BookingDtoForItem.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();

        assertNotNull(bookingDtoForItem);
        assertEquals(1L, bookingDtoForItem.getItemId());
        assertNotNull(bookingDtoForItem.getStart());
        assertNotNull(bookingDtoForItem.getEnd());
    }
}
