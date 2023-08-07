package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingDtoTest {
    @Test
    public void createBookingDtoTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .bookerId(2)
                .item(new Item())
                .status(Status.APPROVED)
                .build();

        assertNotNull(bookingDto);
        assertEquals(1L, bookingDto.getId());
        assertNotNull(bookingDto.getStart());
        assertNotNull(bookingDto.getEnd());
        assertEquals(2, bookingDto.getBookerId());
        assertNotNull(bookingDto.getItem());
        assertEquals(Status.APPROVED, bookingDto.getStatus());
    }
}
