package ru.practicum.shareit.booking.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StatusTest {
    @Test
    public void testStatusEnum() {
        Status waiting = Status.WAITING;
        Status approved = Status.APPROVED;
        Status rejected = Status.REJECTED;
        Status canceled = Status.CANCELED;

        assertEquals("WAITING", waiting.name());
        assertEquals("APPROVED", approved.name());
        assertEquals("REJECTED", rejected.name());
        assertEquals("CANCELED", canceled.name());

        assertEquals(0, waiting.ordinal());
        assertEquals(1, approved.ordinal());
        assertEquals(2, rejected.ordinal());
        assertEquals(3, canceled.ordinal());
    }
}
