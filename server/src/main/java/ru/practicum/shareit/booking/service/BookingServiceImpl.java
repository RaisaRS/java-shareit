package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mappers.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    public BookingDto saveBooking(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден."));

        Booking booking = BookingMapper.toBooking(user, item, bookingDto);

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

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public Booking confirmOrCancelBooking(Long userId, Long bookingId, boolean approved) {
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

        if (!booking.getItem().getOwnerId().equals(userId)) {
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
    public Booking getBookingForOwnerOrBooker(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if (!booking.getItem().getOwnerId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
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
    public List<Booking> getAllBookingsForUser(Long userId, String state, boolean isOwner, int from, int size) {
        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        LocalDateTime timeNow = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        switch (state) {
            case "ALL":
                if (isOwner) {
                    return bookingRepository.getBookingListByOwnerId(userId, pageable);
                } else {
                    return bookingRepository.getBookingListByBookerId(userId, pageable);
                }
            case "REJECTED":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable);
                }
            case "WAITING":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);
                }
            case "CURRENT":
                if (isOwner) {
                    return bookingRepository.getAllCurrentBookingsByOwner(userId, timeNow, timeNow, pageable);
                } else {
                    return bookingRepository.getAllCurrentBookingsByBooker(userId, timeNow, timeNow, pageable);
                }
            case "PAST":
                if (isOwner) {
                    return bookingRepository.getAllPastBookingsByOwner(userId, timeNow, pageable);
                } else {
                    return bookingRepository.getAllPastBookingsByBooker(userId, timeNow, pageable);
                }
            case "FUTURE":
                if (isOwner) {
                    return bookingRepository.getAllFutureBookingsByOwner(userId, timeNow, pageable);
                } else {
                    return bookingRepository.getAllFutureBookingsByBooker(userId, timeNow, pageable);
                }
            default:
                throw new UnsupportedStateException("Unknown state: "
                        + state);
        }
    }
}
