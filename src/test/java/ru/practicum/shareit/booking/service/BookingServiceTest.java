package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.mappers.BookingMapper.toBookingDto;
import static ru.practicum.shareit.mappers.BookingMapper.toBookingFromBookingDtoForItem;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void saveBookingTest() {
        User owner = new User();
        Long ownerId = 1L;
        owner.setId(ownerId);

        User booker = new User();
        Long userId = 2L;
        booker.setId(userId);

        Item item = new Item();
        Long itemId = 1L;
        item.setOwnerId(owner.getId());
        item.setId(itemId);
        item.setAvailable(true);

        BookingDtoForItem request = new BookingDtoForItem();
        request.setItemId(itemId);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusHours(1));
        Booking booking = toBookingFromBookingDtoForItem(request, booker, item);
        booking.setStatus(Status.WAITING);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(toBookingDto(booking).getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDto expect = toBookingDto(booking);
        BookingDto actual = bookingService.saveBooking(userId, toBookingDto(booking));
        assertEquals(expect, actual);
    }

    @Test
    void saveBookingWhenItemNotAvailableThenItemNotAvailableException() {
        User owner = new User();
        Long ownerId = 1L;
        owner.setId(ownerId);

        User booker = new User();
        Long userId = 2L;
        booker.setId(userId);

        Item item = new Item();
        Long itemId = 1L;
        item.setOwnerId(owner.getId());
        item.setId(itemId);
        item.setAvailable(false);

        BookingDtoForItem request = new BookingDtoForItem();
        request.setItemId(itemId);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusHours(1));
        Booking booking = toBookingFromBookingDtoForItem(request, booker, item);
        booking.setStatus(Status.WAITING);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(toBookingDto(booking).getItemId())).thenReturn(Optional.of(item));

        assertThrows(
                ItemUnavailableException.class,
                () -> bookingService.saveBooking(userId, toBookingDto(booking))
        );
    }

    @Test
    void createBookingWhenBookerIdEqualsOwnerIdThenItemNotFoundException() {
        User owner = new User();
        Long ownerId = 1L;
        owner.setId(ownerId);

        User booker = new User();
        Long userId = 1L;
        booker.setId(userId);

        Item item = new Item();
        Long itemId = 1L;
        item.setOwnerId(owner.getId());
        item.setId(itemId);
        item.setAvailable(true);

        BookingDtoForItem request = new BookingDtoForItem();
        request.setItemId(itemId);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusHours(1));
        Booking booking = toBookingFromBookingDtoForItem(request, booker, item);
        booking.setStatus(Status.WAITING);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(toBookingDto(booking).getItemId())).thenReturn(Optional.of(item));

        assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.saveBooking(userId, toBookingDto(booking))
        );
    }

}
