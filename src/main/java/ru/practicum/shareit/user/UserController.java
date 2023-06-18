package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

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
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable @Valid long userId) {
        log.info("Получен GET-запрос /userId {} ", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос /users {} ", userDto);
        return userService.saveUser(userDto);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@Valid @PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("Получен DELETE-запрос /users/:userId {} ", userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        userDto.setId(userId);
        log.info("Получен PATCH-запрос /userId {} ", userId);
        return userService.updateUser(userDto);
    }
}
