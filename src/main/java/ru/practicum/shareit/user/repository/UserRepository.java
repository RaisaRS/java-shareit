package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User deleteUser(long userId);

    User getUserById(long userId);

}
