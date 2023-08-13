package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Request request;
    private User user;
    private User otherUser;

    @BeforeEach
    public void setUp() {

        user = userRepository.save(User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());
        otherUser = userRepository.save(User.builder()
                .id(2L)
                .name("Name")
                .email("iva1n@mail.ru")
                .build());

        Item item = itemRepository.save(Item.builder()
                .id(1L)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1L)
                .ownerId(user.getId())
                .build());


        request = itemRequestRepository.save(Request.builder()
                .id(1L)
                .created(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(user)
                .items(Collections.singletonList(item))
                .build());
        System.out.println(request);
    }

    @AfterEach
    public void tearDown() {
        itemRequestRepository.deleteAll();
    }

    @Test
    void findAllByRequestorIdTest() {
        List<Request> requestList = itemRequestRepository.findAllByRequestorId(1L);
        assertNotNull(requestList);
    }

    @Test
    void findByOwnerIdTest() {
        List<Request> requestList = itemRequestRepository.findByOwnerId(otherUser.getId(), null);

        assertEquals(requestList.get(0).getId(), request.getId());
        assertEquals(requestList.get(0).getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertEquals(requestList.get(0).getCreated(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(requestList.get(0).getRequestor().getId(), user.getId());
        assertEquals(requestList.get(0).getRequestor().getName(), "Ivan");
        assertEquals(requestList.get(0).getRequestor().getEmail(), "ivan@mail.ru");

        List<Request> requestList1 = itemRequestRepository.findByOwnerId(user.getId(), null);
        assertEquals(requestList1.size(), 0);
    }

}
