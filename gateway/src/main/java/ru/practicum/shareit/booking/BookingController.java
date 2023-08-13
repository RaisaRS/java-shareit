package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveBooking(
            @RequestHeader(name = USER_ID_HEADER)
            @Min(value = 1, message = "User ID must be more than 0") Long userId,
            @RequestBody @Validated BookingDto bookingDto
    ) {
        log.info("Получен POST-запрос /bookings {} ", bookingDto);
        ResponseEntity<Object> response = bookingClient.saveBooking(userId, bookingDto);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(name = USER_ID_HEADER)
                                                @Min(value = 1, message = "User ID must be more than 0")
                                                Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam boolean approved) {
        log.info("Получен PATCH-запрос /bookingId подтверждения/отмены бронирования");
        ResponseEntity<Object> response = bookingClient.updateBooking(userId, bookingId, approved);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingForOwnerOrBooker(@RequestHeader(name = USER_ID_HEADER)
                                                             @Min(value = 1, message = "User ID must be more than 0")
                                                             Long userId,
                                                             @PathVariable @Min(value = 0,
                                                                     message = "Booking ID must be more than 0")
                                                             Long bookingId) {
        log.info("Получен GET-запрос просмотра бронирования владельцем предмета или" +
                " пользователем, бронирующим предмет");
        ResponseEntity<Object> response = bookingClient.getBooking(userId, bookingId);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsForBooker(@RequestHeader(name = USER_ID_HEADER)
                                                          @Min(value = 1, message = "User ID must be more than 0")
                                                          Long userId,
                                                          @RequestParam(name="state", defaultValue = "ALL")
                                                          String stateParam,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                              @Min(value = 0, message = "Parameter 'from' must be " +
                                                                      "more than 0") int from,
                                                          @RequestParam(required = false, defaultValue = "10")
                                                              @Min(value = 0, message = "Parameter 'size' must be " +
                                                                      "more than 0") int size) {
        log.info("Получен GET-запрос просмотра всех забронированных вещей и статусов их бронирования " +
                "для  пользователя");
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Looking for bookings of owner {} with state {}", userId, stateParam);
        ResponseEntity<Object> response = bookingClient.getUserBookings(userId, state, from, size);
        return response;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForOwner(@RequestHeader(name = USER_ID_HEADER)
                                                         @Min(value = 1, message = "User ID must be more than 0")
                                                         Long ownerId,
                                                         @RequestParam(name = "state", defaultValue = "ALL")
                                                         String stateParam,
                                                         @RequestParam(required = false, defaultValue = "0")
                                                         @Min(value = 0, message = "Parameter 'from' must be " +
                                                                 "more than 0") int from,
                                                         @RequestParam(required = false, defaultValue = "10")
                                                             @Min(value = 0, message = "Parameter 'size' must be " +
                                                                     "more than 0") int size) {
        log.info("Получен GET-запрос просмотра всех забронированных вещей и статусов их бронирования " +
                "для владельца");
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        ResponseEntity<Object> response = bookingClient.getOwnerBookings(ownerId, state, from, size);
        return response;
    }
}