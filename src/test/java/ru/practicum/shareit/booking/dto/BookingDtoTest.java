package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.enums.Status;

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
                .itemId(3L)
                .item(new BookingDtoItem(1L, "name"))
                .booker(new BookingDtoUser(1L))
                .status(Status.APPROVED)
                .build();

        assertNotNull(bookingDto);
        assertEquals(1L, bookingDto.getId());
        assertNotNull(bookingDto.getStart());
        assertNotNull(bookingDto.getEnd());
        assertEquals(2, bookingDto.getBookerId());
        assertEquals(3L, bookingDto.getItemId());
        assertNotNull(bookingDto.getItem());
        assertNotNull(bookingDto.getBooker());
        assertEquals(Status.APPROVED, bookingDto.getStatus());
    }
}
