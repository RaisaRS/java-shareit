package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemClientTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testValidateItemEpmtyName() throws Exception {

        ItemDto itemDto = ItemDto.builder()
                .name("")
                .description("item description")
                .available(true)
                .build();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    void testValidateItemEpmtyDescription() throws Exception {

        ItemDto itemDto = ItemDto.builder()
                .name("item name")
                .description("")
                .available(true)
                .build();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}