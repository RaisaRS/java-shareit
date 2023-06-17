package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private long id = 0;

    @Override
    public Collection<UserDto> getAllUsers() {
        Collection<User> allUsers = userRepository.getAllUsers();
        return allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
            return UserMapper.toUserDto((userRepository.saveUser(UserMapper.toUser(userDto))));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
            return UserMapper.toUserDto(userRepository.updateUser(UserMapper.toUser(userDto)));
        }

    @Override
    public User getUserById(long userId) {
        return userRepository.getUserById(userId);
    }
}
