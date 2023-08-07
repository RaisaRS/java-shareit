package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());

        item = itemRepository.save(Item.builder()
                .id(1L)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1L)
                .ownerId(user.getId())
                .build());
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void searchItemTest() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Item> itemList = itemRepository.searchItem("Щётка".toLowerCase(), pageable);

        assertEquals(itemList.get(0).getId(), item.getId());
        assertEquals(itemList.get(0).getName(), "Щётка для обуви");
        assertEquals(itemList.get(0).getDescription(), "Стандартная щётка для обуви");
    }
}
