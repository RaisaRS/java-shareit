package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mappers.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.mappers.UserMapper.*;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получен GET-апрос /users");
        return userService.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable @Valid Long userId) {
        log.info("Получен GET-запрос /userId {} ", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveUser(@NotNull @Valid @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос /users {} ", userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.saveUser(user));
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@Valid @PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Получен DELETE-запрос /users/:userId {} ", userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        User user = UserMapper.toUserWithId(userId, userDto);
        log.info("Получен PATCH-запрос /userId {} ", userId);
        return UserMapper.toUserDto(userService.updateUser(user));
    }
}
