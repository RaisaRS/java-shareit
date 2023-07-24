package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получен список всех пользователей");
        return  userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        log.info("Создан пользователь, id = {} ", user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        log.info("Удалён пользователь, id = {} ", id);
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        User oldUser =userRepository.findById(user.getId()).orElseThrow(()->
                new UserNotFoundException("Пользователь не найден " + user.getId()));
        if(user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if(user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.info("Данные пользователя {} обновлены ", user);
        return userRepository.save(oldUser);
    }

    @Override
    public User getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("Пользователь не найден " + id));
        log.info("Получен пользователь, id = {} ", id);
        return user;
    }
}
