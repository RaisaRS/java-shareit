package ru.practicum.shareit.booking.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StateTest {
    @Test
    public void testStateEnumValues() {
        State[] expectedValues = {State.CURRENT, State.PAST, State.FUTURE, State.APPROVED, State.WAITING, State.REJECTED};

        State[] actualValues = State.values();

        assertEquals(expectedValues.length, actualValues.length);
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], actualValues[i]);
        }
    }
}
