package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public BookingDto saveBooking(long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден."));

        Booking booking = toBooking(user, item, bookingDto);

        if (!booking.getEnd().isAfter(booking.getStart())) {
            log.debug("Некорректная дата бронировния");
            throw new InCorrectDateException("Некорректная дата бронировния");
        }

        if ((booking.getItem().getOwnerId()).equals(booking.getBooker().getId())) {
            log.debug("Попытка бронирования своего же предмета отклонена");
            throw new ItemNotFoundException("Бронь своего предмета: " + booking.getItem());
        }

        if (!booking.getItem().getAvailable()) {
            log.debug("В данный момент предмет: {} не может быть забронирован. ", booking.getItem());
            throw new ItemUnavailableException("Предмет недоступен для аренды в данный момент: "
                    + booking.getItem());
        }
        booking.setItem(item);
        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }
        log.info("Предмет {} ожидает подтверждения бронирования от владельца: {} ",
                booking.getItem(), booking.getItem().getOwnerId());

        return toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public Booking confirmOrCancelBooking(long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if (booking.getStatus() == Status.APPROVED) {
            log.debug("Бронирование уже подтверждено ранее");
            throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (booking.getItem().getOwnerId() != userId) {
            log.debug("Подтвердить/отклонить бронирование может только владелец предмета");
            throw new BookingNotFoundException("Подтвердить/отклонить бронирование может только " +
                    "владелец предмета - " + userId);
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking getBookingForOwnerOrBooker(long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if (booking.getItem().getOwnerId() != userId && booking.getBooker().getId() != userId) {
            log.debug("Просмотр бронирования доступен только владельцу предмета или его арендатору");
            throw new BookingNotFoundException("Просмотр бронирования доступен только владельцу предмета" +
                    " или его арендатору");
        }

        if (!booking.getItem().getAvailable()) {
            log.debug("Предмет недоступен для бронирования в данный момент");
            throw new ItemUnavailableException("Предмет недоступен для бронирования в данный момент");
        }

        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> getAllBookingsForUser(long userId, String state, boolean isOwner) {
        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case "ALL":
                if (isOwner) {
                    return bookingRepository.getBookingListByOwnerId(userId);
                } else {
                    return bookingRepository.getBookingListByBookerId(userId);
                }
            case "REJECTED":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                }
            case "WAITING":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                }
            case "CURRENT":
                if (isOwner) {
                    return bookingRepository.getAllCurrentBookingsByOwner(userId, timeNow);
                } else {
                    return bookingRepository.getAllCurrentBookingsByBooker(userId, timeNow);
                }
            case "PAST":
                if (isOwner) {
                    return bookingRepository.getAllPastBookingsByOwner(userId, timeNow);
                } else {
                    return bookingRepository.getAllPastBookingsByBooker(userId, timeNow);
                }
            case "FUTURE":
                if (isOwner) {
                    return bookingRepository.getAllFutureBookingsByOwner(userId, timeNow);
                } else {
                    return bookingRepository.getAllFutureBookingsByBooker(userId, timeNow);
                }
            default:
                throw new UnsupportedStateException("Unknown state: "
                        + state);
        }
    }
}
