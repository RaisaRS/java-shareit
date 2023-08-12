package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager entityManager;

    private User booker1;
    private User booker2;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;

    @BeforeEach
    void setUp() {
        booker1 = userRepository.save(User.builder()
                .id(1L)
                .name("Raisa")
                .email("Raisa@mail.ru")
                .build());

        booker2 = userRepository.save(User.builder()
                .id(2L)
                .name("Name")
                .email("Name@mail.ru")
                .build());

        item1 = itemRepository.save(Item.builder()
                .id(1L)
                .name("Робот-пылесос")
                .description("Лучший работник на планете")
                .ownerId(booker1.getId())
                .available(true)
                .request(1L)
                .build());

        item2 = itemRepository.save(Item.builder()
                .id(2L)
                .name("Пауэрбанк")
                .description("Зарядное устройство для телефона")
                .ownerId(booker2.getId())
                .available(true)
                .request(1L)
                .build());

        booking1 = bookingRepository.save(Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMonths(1))
                .end(LocalDateTime.now().plusYears(1))
                .booker(booker1)
                .item(item1)
                .status(Status.REJECTED)
                .build());

        booking2 = bookingRepository.save(Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusYears(1))
                .end(LocalDateTime.now().plusYears(2))
                .booker(booker2)
                .item(item2)
                .status(Status.REJECTED)
                .build());

        booking3 = bookingRepository.save(Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().minusMonths(1))
                .end(LocalDateTime.now().plusYears(1))
                .booker(booker2)
                .item(item2)
                .status(Status.WAITING)
                .build());

        booking4 = bookingRepository.save(Booking.builder()
                .id(4L)
                .start(LocalDateTime.now().minusYears(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(booker2)
                .item(item2)
                .status(Status.REJECTED)
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertNotNull(entityManager);
    }

    @Test
    void getAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        User booker = userRepository.findById(booker1.getId()).get();
        List<Booking> bookingList = bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(
                booker.getId(), Status.REJECTED, null);
        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Raisa");
        assertEquals(bookingList.get(0).getStart(),
                booking1.getStart());
        assertEquals(bookingList.get(0).getEnd(),
                booking1.getEnd());

    }

    @Test
    void getAllByBookerIdAndStatusOrderByStartDescTest() {
        List<Booking> bookingList = bookingRepository
                .getAllByBookerIdAndStatusOrderByStartDesc(booker2.getId(), Status.WAITING, null);

        assertEquals(bookingList.get(0).getId(), booking3.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Name");
        assertEquals(bookingList.get(0).getStart(),
                booking3.getStart());
        assertEquals(bookingList.get(0).getEnd(),
                booking3.getEnd());

    }

    @Test
    void getBookingListByBookerIdTest() {
        User booker = userRepository.findById(booker1.getId()).get();
        Long bookerId = booker.getId();
        List<Booking> bookingList = bookingRepository.getBookingListByBookerId(bookerId, null);

        assertEquals(bookingList.get(0).getId(), booking1.getId());
    }

    @Test
    void getBookingListByOwnerIdTest() {
        User owner = userRepository.findById(booker2.getId()).get();
        Long ownerId = owner.getId();
        List<Booking> bookingList = bookingRepository.getBookingListByBookerId(ownerId, null);

        assertEquals(bookingList.get(0).getId(), booking2.getId());
    }

    @Test
    void getAllPastBookingsByOwnerTest() {
        User owner = userRepository.findById(booker1.getId()).get();
        Long ownerId = owner.getId();
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
        List<Booking> bookingList = bookingRepository.getAllPastBookingsByOwner(ownerId, localDateTime, null);

        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Raisa");
        assertEquals(bookingList.get(0).getStart(),
                booking1.getStart());
        assertEquals(bookingList.get(0).getEnd(),
                booking1.getEnd());
    }

    @Test
    void getAllFutureBookingsByOwnerTest() {
        User owner = userRepository.findById(booker1.getId()).get();
        Long ownerId = owner.getId();
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        List<Booking> bookingList = bookingRepository.getAllFutureBookingsByOwner(ownerId, localDateTime, null);

        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Raisa");
        assertEquals(bookingList.get(0).getStart(),
                booking1.getStart());
        assertEquals(bookingList.get(0).getEnd(),
                booking1.getEnd());
    }

    @Test
    void getAllFutureBookingsByBookerTest() {
        User booker = userRepository.findById(booker2.getId()).get();
        Long bookerId = booker.getId();
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        List<Booking> bookingList = bookingRepository.getAllPastBookingsByOwner(bookerId, localDateTime, null);

        assertEquals(bookingList.get(0).getId(), booking4.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Name");
        assertEquals(bookingList.get(0).getStart(), booking4.getStart());
        assertEquals(bookingList.get(0).getEnd(), booking4.getEnd());
    }

    @Test
    void getAllCurrentBookingsByBookerTest() {
        User booker = userRepository.findById(booker2.getId()).get();
        Long bookerId = booker.getId();
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        LocalDateTime time1 = LocalDateTime.now().plusDays(1);
        List<Booking> bookingList = bookingRepository.getAllCurrentBookingsByBooker(bookerId, time, time1, null);

        assertEquals(bookingList.get(1).getId(), booking4.getId());
        assertEquals(bookingList.get(1).getBooker().getName(), "Name");
        assertEquals(bookingList.get(1).getStart(), booking4.getStart());
        assertEquals(bookingList.get(1).getEnd(), booking4.getEnd());
    }

    @Test
    void getAllCurrentBookingsByOwnerTest() {
        User owner = userRepository.findById(booker1.getId()).get();
        Long ownerId = owner.getId();
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        LocalDateTime time1 = LocalDateTime.now().plusDays(1);
        List<Booking> bookingList = bookingRepository.getAllCurrentBookingsByBooker(ownerId, time, time1, null);

        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Raisa");
        assertEquals(bookingList.get(0).getStart(), booking1.getStart());
        assertEquals(bookingList.get(0).getEnd(), booking1.getEnd());
    }
}