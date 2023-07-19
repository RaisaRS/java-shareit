package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    //@Autowired
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    //@Autowired
    private final BookingService bookingService;

//    @Autowired
//    public BookingController(@Qualifier(value = "BookingServiceImpl") BookingService bookingService) {
//        this.bookingService = bookingService;
//    }

    @PostMapping
    public BookingDto saveBooking(@RequestHeader(name = USER_ID_HEADER) long userId,
                                  @RequestBody BookingDto bookingDto) {
         log.info("Получен POST-запрос /bookings {} ", bookingDto);
         return toBookingDto(bookingService.saveBooking(userId, bookingDto));
     }

     @PatchMapping("/{bookingId}")
    public BookingDto confirmOrCancelBooking(@RequestHeader(name = USER_ID_HEADER) long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam boolean confirmed) {
         log.info("Получен PATCH-запрос /bookingId подтверждения/отмены бронирования");
         return toBookingDto(bookingService.confirmOrCancelBooking(userId, bookingId, confirmed));
     }
    //confirm or cancel your booking

    @GetMapping("/{bookingId}")
    public BookingDto getBookingForOwnerOrBooker(@RequestHeader(name = USER_ID_HEADER) long userId,
                                                 @PathVariable Long bookingId) {
         log.info("Получен GET-запрос просмотра бронирования владельцем предмета или" +
                 " пользователем, бронирующим предмет");
         return toBookingDto(bookingService.getBookingForOwnerOrBooker(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForBooker(@RequestHeader(name = USER_ID_HEADER) long userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
         log.info("Получен GET-запрос просмотра всех забронированных вещей и статусов их бронирования " +
                 "для  пользователя");
         return bookingService.getAllBookingsForBooker(userId, state).stream()
                 .map(BookingMapper::toBookingDto)
                 .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(name = USER_ID_HEADER) long userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен GET-запрос просмотра всех забронированных вещей и статусов их бронирования " +
                "для владельца");
        return bookingService.getAllBookingsForOwner(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
