package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingDtoShortTest {
    @Test
    public void createBookingDtoShortTest() {
        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .id(1L)
                .build();

        assertNotNull(bookingDtoShort);
        assertEquals(1L, bookingDtoShort.getId());
    }
}
