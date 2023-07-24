package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {
    BookingDto saveBooking(long userId, BookingDto bookingDtoItem);

    Booking confirmOrCancelBooking(long userId, Long bookingId, boolean approved);

    Booking getBookingForOwnerOrBooker(long userId, Long bookingId);

    Collection<Booking> getAllBookingsForUser(long userId, String state, boolean isOwner);

}
