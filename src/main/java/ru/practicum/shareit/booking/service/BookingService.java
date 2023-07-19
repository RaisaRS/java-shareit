package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {
    Booking saveBooking(long userId, BookingDto bookingDto);
    Booking confirmOrCancelBooking(long userId, Long bookingId, boolean confirmed);
    Booking getBookingForOwnerOrBooker(long userId, Long bookingId);
    Collection<Booking> getAllBookingsForBooker(long userId, String state);
    Collection<Booking> getAllBookingsForOwner(long userId, String state);

}
