package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.mappers.UserMapper.toUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper(); //maybe final
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setUp() {

        user = new User().builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void saveUserTest() {
        when(userRepository.save(any()))
                .thenReturn(user);
        User userSaved = userService.saveUser(user);

        assertNotNull(userSaved);
        assertEquals(user.getId(), userSaved.getId());
    }

    @Test
    void saveUserWithDoubleExceptionTest() {

        Mockito.when(userRepository.save(any()))
                .thenReturn(user)
                .thenThrow(new ConflictException("Пользователь с таким email уже существует"));

        User userSaved = userService.saveUser(user);

        assertNotNull(userSaved);
        assertThrows(
                ConflictException.class,
                () -> userService.saveUser(user)
        );
    }

//    @Test
//    void saveUserWithBadNameTest() {
//        user.setName("");
//        assertThrows(
//                ConstraintViolationException.class,
//                () -> userService.saveUser(user));
//    }
//
//    @Test
//    void saveUserWithBadEmailTest() {
//        user.setEmail("@");
//        assertThrows(
//                ConstraintViolationException.class,
//                () -> userService.saveUser(user));
//    }

    @Test
    void getAllUsersTest() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));
        List<User> expectedResult = List.of(mapper.map(user, User.class));

        assertEquals(expectedResult, userService.getAllUsers());
    }

    @Test
    void updateUserNameAndEmailTest() {

        user.setEmail("ivan@mailupdated.ru");
        user.setName("ivanupdated");

        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);
        User content = userService.updateUser(user);

        assertEquals(content.getEmail(), "ivan@mailupdated.ru");
        assertEquals(content.getName(), "ivanupdated");
    }

//    @Test
//    void updateUserWithBadNameTest() {
//        user.setName("");
//        Mockito.when(userRepository.findById(Mockito.any()))
//                .thenReturn(Optional.ofNullable(user));
//
//        assertThrows(
//                ConstraintViolationException.class,
//                () -> userService.updateUser(user));
//    }

//    @Test
//    void updateUserWithBadEmailTest() {
//
//        user.setEmail("ivan@");
//
//        Mockito.when(userRepository.findById(Mockito.any()))
//                .thenReturn(Optional.ofNullable(user));
//
//        assertThrows(
//                ConstraintViolationException.class,
//                () -> userService.updateUser(user));
//    }

//    @Test
//    void deleteUserWithBadIdTest() {
//        Mockito.when(userRepository.findById(777L))
//                .thenThrow(new UserNotFoundException("Пользователь с id = 777 не найден в базе данных"));
//        UserNotFoundException exception =
//                assertThrows(UserNotFoundException.class,
//                        () -> userService.deleteUser(777L));
//
//        assertEquals("Пользователь с id = 777 не найден в базе данных", exception.getMessage());
//    }


    @Test
    void deleteUserServiceTest() {
        when(userRepository.save(any()))
                .thenReturn(user);
        UserDto userDto = mapper.map(user, UserDto.class);
        assertEquals(userDto.getEmail(), userService.saveUser(toUser(userDto)).getEmail());

        assertThrows(IndexOutOfBoundsException.class,
                () -> userRepository.findAll().get(0));
    }


    @Test
    void getUserByIdTest() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        userService.getUserById(1L);
        assertEquals(user, userService.getUserById(1L));
    }

}