package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingDtoUserTest {
    @Test
    public void createBookingDtoUserTest() {
        BookingDtoUser bookingDtoUser = BookingDtoUser.builder()
                .id(1L)
                .build();

        assertNotNull(bookingDtoUser);
        assertEquals(1L, bookingDtoUser.getId());
    }

}
