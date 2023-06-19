package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAllUsers() {
        Collection<User> allUsers = userRepository.getAllUsers();
        log.info("Получен список всех пользователей");
        return allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("Создан пользователь, id = {} ", userDto);
        return UserMapper.toUserDto((userRepository.saveUser(UserMapper.toUser(userDto))));
    }

    @Override
    public void deleteUser(long userId) {
        log.info("Удалён пользователь, id = {} ", userId);
        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Данные пользователя {} обновлены ", userDto);
        return UserMapper.toUserDto(userRepository.updateUser(UserMapper.toUser(userDto)));
    }

    @Override
    public User getUserById(long userId) {
        log.info("Получен пользователь, id = {} ", userId);
        return userRepository.getUserById(userId);
    }
}
