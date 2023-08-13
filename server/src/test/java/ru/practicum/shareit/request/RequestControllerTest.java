package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {RequestController.class, ErrorHandler.class})
public class RequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ItemRequestService itemRequestService;

    private RequestDto requestDto;
    private RequestDtoWithRequest requestDtoWithRequest;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();


        requestDto = RequestDto.builder()
                .id(1L)
                .created(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(userDto)
                .items(new ArrayList<>())
                .build();

        requestDtoWithRequest = RequestDtoWithRequest.builder()
                .id(1L)
                .created(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(userDto)
                .items(new ArrayList<>())
                .build();

    }

    @Test
    @SneakyThrows
    void addItemRequestTest() {
        when(itemRequestService.addItemRequest(any(), anyLong()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void requestsGetTest() {
        when(itemRequestService.getItemRequest(anyLong()))
                .thenReturn(List.of(requestDtoWithRequest));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void getUserItemRequests() {

        Long userId = userDto.getId();
        RequestDtoWithRequest requestDtoWithRequest = RequestDtoWithRequest.builder()
                .id(requestDto.getId())
                .requestor(requestDto.getRequestor())
                .created(requestDto.getCreated())
                .items(Collections.emptyList())
                .build();

        List<RequestDtoWithRequest> expected = new ArrayList<>(
                List.of(requestDtoWithRequest)
        );

        when(itemRequestService.getItemRequest(anyLong()))
                .thenReturn(expected);


        MvcResult result = mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<RequestDtoWithRequest> actual = List.of(
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        RequestDtoWithRequest[].class
                )
        );

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    void requestsAllGetTest() {
        when(itemRequestService.getAllItemRequest(1L, 0, 20))
                .thenReturn(List.of(requestDtoWithRequest));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void getRequestByIdTest() {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestDtoWithRequest);

        mockMvc.perform(get("/requests/{requestId}", requestDtoWithRequest.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }
}
