package ru.practicum.shareit.request;

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


import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.util.ErrorHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {RequestController.class, ErrorHandler.class})
public class RequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient requestClient;

    private RequestDto requestDto;
    private Long userId = 1L;
    private Long requestId = 1L;
    private Integer from = 0;
    private Integer size = 10;

    @BeforeEach
    public void init() {
        requestDto = RequestDto.builder()
                .description("description")
                .build();
    }

    @SneakyThrows
    @Test
    public void addRequest_Normal() {
        when(requestClient.addItemRequest(userId, requestDto)).thenReturn(
                new ResponseEntity<>(new RequestDto(), HttpStatus.OK));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    public void addRequest_EmptyDescription() {
        requestDto.setDescription("");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        requestDto.setDescription("   ");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(requestClient, never()).addItemRequest(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void findRequest_Normal() {
        when(requestClient.getRequestById(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(new RequestDto(), HttpStatus.OK));

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }



    @SneakyThrows
    @Test
    public void getOwnRequests_Normal() {
        when(requestClient.requestsGet(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    public void getOwnRequests_EmptyFrom() {
        when(requestClient.requestsGet(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "")
                        .param("size", size.toString()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getOwnRequests_EmptySize() {
        when(requestClient.requestsGet(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", ""))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    public void getRequests_Normal() {
        when(requestClient.getRequestsAll(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getRequests_EmptyFrom() {
        when(requestClient.getRequestsAll(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "")
                        .param("size", size.toString()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getRequests_EmptySize() {
        when(requestClient.getRequestsAll(anyLong(), anyInt(), anyInt())).thenReturn(
                new ResponseEntity<>(List.of(requestDto), HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", ""))
                .andExpect(status().isOk());
    }
}