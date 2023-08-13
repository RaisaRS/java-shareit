package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен GET-апрос /users");
        ResponseEntity<Object> response = userClient.getAllUsers();
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable  @Min(value = 1, message = "User ID must be more than 0")
                                                  Long userId) {
        log.info("Получен GET-запрос /userId {} ", userId);
        ResponseEntity<Object> response = userClient.getUser(userId);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@NotNull @Validated(Validation.Post.class) @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос /users {} ", userDto);
        ResponseEntity<Object> response = userClient.addUser(userDto);
        log.info("Ответ на запрос: {}", response);
        return response;
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable @Min(value = 1, message = "User ID must be more than 0") Long userId) {
        log.info("Получен DELETE-запрос /users/:userId {} ", userId);
        ResponseEntity<Object> response = userClient.deleteUser(userId);
        log.info("Ответ на запрос: {}", response);

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@NotNull @Validated(Validation.Patch.class) @RequestBody UserDto userDto,
                              @PathVariable @Min(value = 1,
                                      message = "User ID must be more than 0") Long userId) {
        log.info("Получен PATCH-запрос /userId {} на изменение данных: {} ", userId, userDto);
        ResponseEntity<Object> response = userClient.updateUser(userId, userDto);
        log.info("Ответ на запрос: {}", response);
        return response;
    }
}
