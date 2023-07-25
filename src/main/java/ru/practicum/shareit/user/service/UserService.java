package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(long userId);

    User updateUser(User user);

    User getUserById(long id);
}
