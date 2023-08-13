package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    Booking findFirstBookingByItemIdAndStartIsAfterAndStatusNotLikeOrderByStartAsc(Long itemId,
                                                                                   LocalDateTime dateTime,
                                                                                   Status status);

    Booking findFirstBookingByItemIdAndStartIsBeforeAndStatusNotLikeOrderByStartDesc(Long itemId,
                                                                                     LocalDateTime dateTime,
                                                                                     Status status);

    List<Booking> getAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status, Pageable pageable);

    List<Booking> getAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.booker bb" +
            " WHERE bb.id = ?1 ORDER BY b.start DESC")
    List<Booking> getBookingListByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i " +
            "WHERE i.ownerId = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingListByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i " +
            "WHERE i.ownerId = ?1 AND (b.end < ?2) " +
            "ORDER BY b.start DESC")
    List<Booking> getAllPastBookingsByOwner(Long ownerId, LocalDateTime time, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i " +
            " WHERE i.ownerId = ?1 AND(b.end > ?2) " +
            "ORDER BY b.start DESC")
    List<Booking> getAllFutureBookingsByOwner(Long ownerId, LocalDateTime time, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.booker bb" +
            " WHERE bb.id = ?1 AND (b.end < ?2) " +
            "ORDER BY b.start DESC")
    List<Booking> getAllPastBookingsByBooker(Long bookerId, LocalDateTime timeNow, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.booker bb " +
            "WHERE bb.id = ?1 AND (b.end > ?2) " +
            "ORDER BY b.start DESC")
    List<Booking> getAllFutureBookingsByBooker(Long bookerId, LocalDateTime timeNow, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.booker bb " +
            "WHERE bb.id = ?1 AND (b.start <= ?2 AND b.end >= ?2)" +
            " ORDER BY b.start DESC")
    List<Booking> getAllCurrentBookingsByBooker(Long bookerId, LocalDateTime timeNow, LocalDateTime now1,
                                                Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE i.ownerId = ?1 " +
            "AND (b.start <= ?2 AND b.end >= ?2) " +
            "ORDER BY b.start DESC")
    List<Booking> getAllCurrentBookingsByOwner(Long ownerId, LocalDateTime timeNow, LocalDateTime now1,
                                               Pageable pageable);
}
