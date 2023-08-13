package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public Collection<User> getAllUsers() {
        log.info("Получен список всех пользователей");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    @SneakyThrows
    public User saveUser(User user) {
        log.info("Создан пользователь, id = {} ", user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Удалён пользователь, id = {} ", id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User oldUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + user.getId()));
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.info("Данные пользователя {} обновлены ", user);
        return userRepository.save(oldUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + id));
        log.info("Получен пользователь, id = {} ", id);
        return user;
    }
}
