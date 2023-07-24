package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.Status;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findFirstBookingByItemIdAndEndIsBeforeAndStatusNotLikeOrderByEndDesc(Long itemId,
                                                                                LocalDateTime dateTime,
                                                                                 Status status);
    Booking findFirstBookingByItemIdAndStartIsAfterAndStatusNotLikeOrderByStartAsc(Long itemId,
                                                                                  LocalDateTime dateTime,
                                                                                  Status status);

    Booking findFirstBookingByItemIdAndStartIsBeforeAndStatusNotLikeOrderByStartDesc(Long itemId,
                                                                                    LocalDateTime dateTime,
                                                                                     Status status);
    Collection<Booking> getAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, Status status);

    Collection<Booking> getAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);
    @Query("SELECT b FROM Booking AS b JOIN b.booker AS bb" +
            " WHERE bb.id = ?1 ORDER BY b.start DESC")
    Collection<Booking> getBookingListByBookerId(Long bookerId);

    @Query("SELECT b FROM Booking AS b JOIN b.item AS i " +
            "WHERE i.ownerId = ?1 " +
            "ORDER BY b.start DESC")
    Collection<Booking> getBookingListByOwnerId(long ownerId);

    @Query("SELECT b FROM Booking AS b JOIN b.item AS i " +
            "WHERE i.ownerId = ?1 AND (b.end < ?2) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getAllPastBookingsByOwner(long ownerId, LocalDateTime time);

    @Query("SELECT b FROM Booking AS b JOIN b.item AS i " +
            " WHERE i.ownerId = ?1 AND(b.end > ?2) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getAllFutureBookingsByOwner(long ownerId, LocalDateTime time);

    @Query("SELECT b FROM Booking  AS b JOIN b.booker AS bb" +
            " WHERE bb.id = ?1 AND (b.end < ?2) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getAllPastBookingsByBooker(long bookerId, LocalDateTime timeNow);

    @Query("SELECT b FROM Booking AS b JOIN b.booker AS bb " +
            "WHERE bb.id = ?1 AND (b.end > ?2) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getAllFutureBookingsByBooker(long bookerId, LocalDateTime timeNow);

    @Query("SELECT b FROM Booking AS b JOIN b.booker AS bb " +
            "WHERE bb.id = ?1 AND (b.start <= ?2 AND b.end >= ?2)" +
            " ORDER BY b.start DESC")
    Collection<Booking> getAllCurrentBookingsByBooker(long bookerId, LocalDateTime timeNow);

    @Query("SELECT b FROM Booking  AS b JOIN b.item AS i WHERE i.ownerId = ?1 " +
            "AND (b.start <= ?2 AND b.end >= ?2) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getAllCurrentBookingsByOwner(long ownerId, LocalDateTime timeNow);
}
