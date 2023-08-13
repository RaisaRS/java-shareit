package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.mappers.BookingMapper.toBooking;
import static ru.practicum.shareit.mappers.BookingMapper.toBookingDto;
import static ru.practicum.shareit.mappers.ItemMapper.toItem;

@WebMvcTest
@ContextConfiguration(classes = {BookingController.class, ErrorHandler.class})
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private UserDto userDto;
    private User user;
    private ItemDto itemDto;


    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();
        user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .requestId(1L)
                .build();


        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 7, 9, 13, 56))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .itemId(1L)
                .booker(userDto)
                .item(itemDto)
                .status(Status.WAITING)
                .build();


    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void setApproveByOwnerCurrentTest() {
        Booking booking = toBooking(user, toItem(user, itemDto), bookingDto);

        when(bookingService.confirmOrCancelBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);
        BookingDto bookingDto1 = toBookingDto(booking);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void getByBookerTest() {
        Booking booking = toBooking(user, toItem(user, itemDto), bookingDto);
        when(bookingService.getBookingForOwnerOrBooker(anyLong(), anyLong()))
                .thenReturn(booking);

        BookingDto bookingDto1 = toBookingDto(booking);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));

    }

}


