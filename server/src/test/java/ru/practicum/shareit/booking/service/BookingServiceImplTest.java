package ru.practicum.shareit.booking.service;

import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.mappers.BookingMapper.toBooking;
import static ru.practicum.shareit.mappers.UserMapper.toUserDto;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private BookingDto bookingDto;
    private BookingDto bookingDto1;
    private BookingDto bookingDto2;
    private BookingDto bookingDto3;
    private Booking booking;
    private UserDto userDto;
    private User user;
    private User user1;
    private ItemDto itemDto;
    private Item item;
    private Item item2;

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
        user1 = User.builder()
                .id(2L)
                .name("Petr")
                .email("petr@mail.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Убирает пыль и грязь")
                .ownerId(user1.getId())
                .available(true)
                .request(1L)
                .build();

        item2 = Item.builder()
                .id(2L)
                .name("Щётка для ванны")
                .description("Стандартная щётка для ванны")
                .ownerId(user.getId())
                .available(true)
                .request(2L)
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
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .booker(toUserDto(user))
                .itemId(2L)
                .status(Status.WAITING)
                .build();

        bookingDto1 = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().minusMonths(1))
                .booker(toUserDto(user1))
                .itemId(1L)
                .status(Status.WAITING)
                .build();

        bookingDto2 = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMonths(1))
                .end(LocalDateTime.now().minusMonths(1))
                .booker(toUserDto(user))
                .itemId(1L)
                .status(Status.WAITING)
                .build();

        bookingDto3 = BookingDto.builder()
                .id(3L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMonths(1))
                .booker(toUserDto(user))
                .itemId(item.getId())
                .status(Status.WAITING)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 7, 9, 13, 56))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveBookingTest() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(2L))
                .thenReturn(Optional.of(item));

        when(bookingRepository.save(any()))
                .thenReturn(booking);
        BookingDto bookingDto5 = bookingService.saveBooking(user.getId(), bookingDto);

        assertEquals(1, bookingDto5.getId());
    }

    @Test
    void saveBookingWrongBookerIdTest() {

        var exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.saveBooking(77L, bookingDto));

        assertEquals("Пользователь не найден 77", exception.getMessage());
    }


    @Test
    void getByBookerTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);
        booking.setItem(item);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Booking booking1 = bookingService.getBookingForOwnerOrBooker(1L, 1L);

        assertEquals(1L, booking1.getId());
    }

    @Test
    void getAllBookingsForUserTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getBookingListByOwnerId(any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "ALL", true, 0, 20));
    }

    @Test
    void getAllForBookerFUTURETest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllFutureBookingsByBooker(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "FUTURE", false, 0, 20));
    }

    @Test
    void getAllForBookerWAITINGTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);
        when(bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));
        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "WAITING", false, 0, 20));
    }

    @Test
    void getAllForBookerREJECTEDTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "REJECTED", false, 0, 20));
    }

    @Test
    void getAllForBookerCURRENTTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllCurrentBookingsByBooker(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "CURRENT", false, 0, 20));
    }

    @Test
    void getAllForBookerPASTTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllPastBookingsByBooker(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                1L, "PAST", false, 0, 20));
    }

    @Test
    void getAllForOwnerTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getBookingListByOwnerId(anyLong(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "ALL", true, 0, 20));
    }

    @Test
    void getAllForOwnerCURRENTTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllCurrentBookingsByOwner(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "CURRENT", true, 0, 20));
    }

    @Test
    void getAllForOwnerPASTTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllPastBookingsByOwner(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "PAST", true, 0, 20));
    }

    @Test
    void getAllForOwnerFUTURETest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllFutureBookingsByOwner(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "FUTURE", true, 0, 20));
    }

    @Test
    void getAllForOwnerWAITINGTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "WAITING", true, 0, 20));
    }

    @Test
    void getAllForOwnerREJECTEDTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);

        when(bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> expectedResult = List.of(booking);

        assertEquals(expectedResult, bookingService.getAllBookingsForUser(
                user.getId(), "REJECTED", true, 0, 20));
    }

    @Test
    void confirmOrCancelBookingTest() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        Booking booking = toBooking(user, item, bookingDto);
        booking.setItem(item2);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking1 = bookingService.confirmOrCancelBooking(user.getId(), 3L, true);

        assertEquals(booking.getStatus(), booking1.getStatus());
    }

    @Test
    void updateBookingWithWrongBookerTest() {

        var exception = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.confirmOrCancelBooking(77L, 1L, true));
        assertEquals("Бронирование не найдено",
                exception.getMessage());
    }

}
