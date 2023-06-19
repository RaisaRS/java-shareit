package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User saveUser(User user) {
        if (users.values().stream().map(User::getEmail).anyMatch(s -> s.equals(user.getEmail())))
            throw new ConflictException("Пользователь с таким email уже существует: " + user.getEmail());
        ++id;
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Пользователь создан, {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() != null)
            users.get(user.getId()).setName(user.getName());
        if (user.getEmail() != null)
            if (users.values().stream().filter(u -> u.getId() != user.getId()).anyMatch(u ->
                    u.getEmail().equals(user.getEmail())))
                throw new ConflictException("Пользователь с таким email уже существует: " + user.getEmail());
            else
                users.get(user.getId()).setEmail(user.getEmail());
        log.info("Обновление данных пользователя: {} успешно", user);
        return users.get(user.getId());
    }

    @Override
    public User deleteUser(long userId) {
        checkUser(userId, "Пользователь не найден");
        log.info("Удаление пользователя, id = {}", userId);
        return users.remove(userId);
    }

    @Override
    public User getUserById(long id) {
        if (users.containsKey(id)) {
            log.info("Получение пользователя по id = {}", id);
            return users.get(id);
        } else {
            log.warn("Пользователь не найден, id = {}", id);
            throw new UserNotFoundException("Пользователь не найден id = " + id);
        }
    }

    public void checkUser(long id, String message) {
        if (!users.containsKey(id)) {
            printErrorMessage(message + id);
        }
    }

    private void printErrorMessage(String message) {
        log.error(message);
        throw new UserNotFoundException(message);
    }
}
