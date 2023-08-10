package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RequestDtoTest {
    private RequestDto requestDto;

    @BeforeEach
    public void setUp() {
        requestDto = new RequestDto();
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        requestDto.setId(id);
        assertEquals(id, requestDto.getId());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "This is a test description";
        requestDto.setDescription(description);
        assertEquals(description, requestDto.getDescription());
    }

    @Test
    public void testSetAndGetRequestor() {
        UserDto requestor = new UserDto();
        requestDto.setRequestor(requestor);
        assertNotNull(requestDto.getRequestor());
    }

    @Test
    public void testSetAndGetCreatedTime() {
        LocalDateTime created = LocalDateTime.now();
        requestDto.setCreated(created);
        assertEquals(created, requestDto.getCreated());
    }

    @Test
    public void testSetAndGetItems() {
        List<ItemDtoReq> items = new ArrayList<>();
        items.add(new ItemDtoReq());
        requestDto.setItems(items);
        assertEquals(items, requestDto.getItems());
    }
}
