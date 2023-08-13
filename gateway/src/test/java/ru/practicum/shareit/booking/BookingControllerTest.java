package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.util.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = {BookingController.class, ErrorHandler.class})
public class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    private BookingDto bookingDto;
    private Long userId = 1L;
    private Long bookingId = 1L;
    private Integer from = 0;
    private Integer size = 10;
    private String state = "all";

    @BeforeEach
    public void init() {
        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
    }

    @SneakyThrows
    @Test
    public void saveBookingNormal() {
        when(bookingClient.saveBooking(anyLong(), any(BookingDto.class))).thenReturn(
                new ResponseEntity<>(new BookingDto(), HttpStatus.OK));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void saveBookingWrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).saveBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void saveBookingEmptyStartDate() {
        bookingDto.setStart(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).saveBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void saveBookingEmptyEndDate() {
        bookingDto.setEnd(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).saveBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void saveBookingWrongItemId() {
        bookingDto.setItemId(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        bookingDto.setItemId(-999L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).saveBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void getBookingNormal() {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(new BookingDto(), HttpStatus.OK));

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getBookingWrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getBooking(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void getBookingWrongBookingId() {
        Long wrongBookingId = -999L;

        mockMvc.perform(get("/bookings/" + wrongBookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getBooking(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    public void getUserBookingNormal() {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getUserBookingStateIsNullOrEmpty() {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK));
        String stateNull = null;

        mockMvc.perform(get("/bookings")
                        .param("state", stateNull)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        String stateEmpty = "";

        mockMvc.perform(get("/bookings")
                        .param("state", stateEmpty)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getUserBookingWrongFromOrSize() {
        Integer wrongFrom = -999;

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", wrongFrom.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        Integer wrongSize = -999;

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", wrongSize.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getUserBookings(anyLong(), any(State.class), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void getUserBookingWrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getUserBookings(anyLong(), any(State.class), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void getOwnerBookingNormal() {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getOwnerBookingStateIsNullOrEmpty() {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(
                new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK));
        String stateNull = null;

        mockMvc.perform(get("/bookings/owner")
                        .param("state", stateNull)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        String stateEmpty = "";

        mockMvc.perform(get("/bookings")
                        .param("state", stateEmpty)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getOwnerBookingWrongFromOrSize() {
        Integer wrongFrom = -999;

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", wrongFrom.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        Integer wrongSize = -999;

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", wrongSize.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getUserBookings(anyLong(), any(State.class), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void getOwnerBookingWrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getUserBookings(anyLong(), any(State.class), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    public void updateBookingNormal() {
        when(bookingClient.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(
                new ResponseEntity<>(bookingDto, HttpStatus.OK));

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void updateBookingWrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", wrongUserId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).updateBooking(anyLong(), anyLong(), anyBoolean());
    }


    @SneakyThrows
    @Test
    public void updateBookingWrongApproved() {
        mockMvc.perform(patch("/bookings/" + bookingId)
                        .param("approved", "WRONG PARA")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).updateBooking(anyLong(), anyLong(), anyBoolean());
    }
}