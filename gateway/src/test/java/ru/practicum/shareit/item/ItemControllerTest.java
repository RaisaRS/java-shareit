package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.ErrorHandler;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {ItemController.class, ErrorHandler.class})
public class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto;
    private CommentDto commentDto;
    private Long userId = 1L;
    private Integer from = 0;
    private Integer size = 10;

    @BeforeEach
    public void init() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .text("text")
                .build();
    }


    @SneakyThrows
    @Test
    public void addItem_Normal() {
        when(itemClient.saveItem(any(ItemDto.class), anyLong())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    public void addItem_EmptyName() {
        itemDto.setName("");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        itemDto.setName("   ");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).saveItem(any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void addItem_EmptyDescription() {
        itemDto.setDescription("");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        itemDto.setDescription("   ");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).saveItem(any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void addItem_EmptyAvailable() {
        itemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).saveItem(any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void addItem_WrongUserId() {
        Long wrongId = -9999L;

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", wrongId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).saveItem(any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void updateItem_Normal() {
        when(itemClient.updateItem(any(ItemDto.class), anyLong())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));
        Long itemId = 1L;

        mockMvc.perform(patch("/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void updateItem_WrongUserId() {
        Long wrongUserId = -999L;
        Long itemId = 1L;

        mockMvc.perform(patch("/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).updateItem( any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void updateItem_WrongItemId() {
        Long wrongItemId = -999L;

        mockMvc.perform(patch("/items/" + wrongItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).updateItem( any(), anyLong());
    }

    @SneakyThrows
    @Test
    public void getAllItems_Normal() {
        when(itemClient.getItemByUser(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getAllItems_WrongId() {
        Long wrongUserId = -999L;

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).getItemByUser(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void getAllItems_WrongFromOrSize() {
        Integer wrongFrom = -999;

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", wrongFrom.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        Integer wrongSize = -999;

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.toString())
                        .param("size", wrongSize.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).getItemByUser(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void getItem_Normal() {
        when(itemClient.getItemById(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));
        Long itemId = 1L;

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getItem_WrongUserId() {
        Long itemId = 1L;
        Long wrongUserId = -999L;

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).getItemById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void getItem_WrongItemId() {
        Long wrongItemId = -999L;

        mockMvc.perform(get("/items/" + wrongItemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).getItemById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void searchItem_Normal() {
        when(itemClient.searchItem(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));

        String text = "this is text";

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void searchItem_wrongUserId() {
        Long wrongUserId = -999L;

        String text = "this is text";

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", wrongUserId)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).searchItem(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void searchItem_WrongFromOrSize() {
        Integer wrongFrom = -999;
        String text = "this is text";

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", wrongFrom.toString())
                        .param("size", size.toString())
                        .param("text", text))
                .andExpect(status().isBadRequest());

        Integer wrongSize = -999;

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", wrongSize.toString())
                        .param("text", text))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).searchItem(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void deleteItem_Normal() {
        when(itemClient.deleteItem(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(new ItemDto(), HttpStatus.OK));
        Long itemId = 1L;

        mockMvc.perform(delete("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteItem_WrongUserOrItemId() {
        Long wrongItemId = -999L;

        mockMvc.perform(delete("/items/" + wrongItemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        Long wrongUserId = -999L;
        Long itemId = 1L;

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).deleteItem(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void addComment_Normal() {
        when(itemClient.postComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(
                new ResponseEntity<>(new CommentDto(), HttpStatus.OK));
        Long itemId = 1L;

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void addComment_EmptyText() {
        Long itemId = 1L;
        commentDto.setText("");

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        commentDto.setText("   ");

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).deleteItem(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void addComment_WrongUserId() {
        Long itemId = 1L;
        Long wrongUsrId = -999L;

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", wrongUsrId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).postComment(anyLong(), anyLong(), any(CommentDto.class));
    }

    @SneakyThrows
    @Test
    public void addComment_WrongIyemId() {
        Long wrongItemId = -999L;

        mockMvc.perform(post("/items/" + wrongItemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).postComment(anyLong(), anyLong(), any(CommentDto.class));
    }
}
