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
    public Booking saveBooking(long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = itemRepository.findById(bookingDto.getItem().getId()).orElseThrow(()->
                new ItemNotFoundException("Предмет не найден."));

        Booking booking = toBooking(user, item, bookingDto);

        if((booking.getItem().getOwnerId()).equals(booking.getBooker().getId())) {
            log.debug("Попытка бронирования своего же предмета отклонена");
            throw new ItemNotFoundException("Бронь своего предмета: " + booking.getItem());
        }

        if(!booking.getItem().getAvailable()) {
            log.debug("В данный момент предмет: {} не может быть забронирован. ", booking.getItem());
            throw new ItemNotFoundException("Предмет недоступен для аренды в данный момент: "
                    + booking.getItem());
        }

        if(!booking.getEnd().isAfter(booking.getStart())) {
            log.debug("Дата окончания бронирования не может быть ранее даты начала бронирования");
            throw new InCorrectDateException("Введена некорректная дата бронирования");
        }

        booking.setState(State.WAITING);
        log.info("Предмет {} ожидает подтверждения бронирования от владельца: {} ",
                booking.getItem(), booking.getItem().getOwnerId());

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmOrCancelBooking(long userId, Long bookingId, boolean confirmed) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if(!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if(booking.getState() == State.APPROVED) {
            log.debug("Бронирование уже подтверждено ранее");
            throw new InCorrectBookingException("Бронирование уже подтверждено ранее");
        }

        if(booking.getItem().getOwnerId() != userId) {
            log.debug("Подтвердить/отклонить бронирование может только владелец предмета");
            throw new BookingNotFoundException("Подтвердить/отклонить бронирование может только " +
                    "владелец предмета - " + userId);
        }

        if(confirmed) {
            booking.setState(State.APPROVED);
        } else {
            booking.setState(State.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking getBookingForOwnerOrBooker(long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if(!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if(booking.getItem().getOwnerId() != userId && booking.getBooker().getId() != userId) {
            log.debug("Просмотр бронирования доступен только владельцу предмета или его арендатору");
            throw new BookingNotFoundException("Просмотр бронирования доступен только владельцу предмета" +
                    " или его арендатору");
        }

        if(booking.getItem().getAvailable()) {
            log.debug("Предмет недоступен для бронирования в данный момент");
            throw new BookingNotFoundException("Предмет недоступен для бронирования в данный момент");
        }

        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> getAllBookingsForBooker(long bookerId, String state) {
        if(userRepository.existsById(bookerId)) {
            log.debug("Пользователь {} не найден ", bookerId);
            throw new UserNotFoundException("Пользователь не найден " + bookerId);
        }

        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case "ALL":
                return bookingRepository.getBookingListByBookerId(bookerId);
            case "REJECTED":
                return bookingRepository.getAllByBookerIdAndStateOrderByStartDesc(bookerId, State.REJECTED);
            case "WAITING":
                return bookingRepository.getAllByBookerIdAndStateOrderByStartDesc(bookerId, State.WAITING);
            case "CURRENT":
                return bookingRepository.getAllCurrentBookingsByBooker(bookerId, timeNow);
            case "PAST":
                return bookingRepository.getAllPastBookingsByBooker(bookerId, timeNow);
            case "FUTURE":
                return bookingRepository.getAllFutureBookingsByBooker(bookerId, timeNow);
            default:
                throw new InCorrectStatusException("Неверный статус бронирования " + state);
        }
    }

    @Override
    public Collection<Booking> getAllBookingsForOwner(long ownerId, String state) {
        if(userRepository.existsById(ownerId)) {
            log.debug("Пользователь {} не найден ", ownerId);
            throw new UserNotFoundException("Пользователь не найден " + ownerId);
        }

        LocalDateTime timeNow = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository.getBookingListByOwnerId(ownerId);
            case "REJECTED":
                return bookingRepository.getAllByBookerIdAndStateOrderByStartDesc(ownerId, State.REJECTED);
            case "WAITING":
                return bookingRepository.getAllByItemOwnerIdAndStateOrderByStartDesc(ownerId, State.WAITING);
            case "CURRENT":
                return bookingRepository.getAllCurrentBookingsByOwner(ownerId, timeNow);
            case "PAST":
                return bookingRepository.getAllPastBookingsByOwner(ownerId, timeNow);
            case "FUTURE":
                return bookingRepository.getAllFutureBookingsByOwner(ownerId, timeNow);
            default:
                throw new InCorrectStatusException("Неверный статус бронирования " + state);
        }
    }
}
