package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.MethodArgumentNotValidException;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.mappers.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestDto addItemRequest(RequestDto requestDto, Long userId) {
        Optional<User> newUser = userRepository.findById(userId);
        if (newUser.isPresent()) {
            User user = newUser.get();

            Request request = RequestMapper.toItemRequest(user, requestDto);
            request.setRequestor(user);
            request.setCreated(LocalDateTime.now());

            return RequestMapper.toItemRequestDto(itemRequestRepository.save(request));
        } else {
            throw new UserNotFoundException("Пользователь с id " + "userId" + "не найден");
        }
    }


    @Override
    public List<RequestDtoWithRequest> getItemRequest(Long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        List<RequestDtoWithRequest> requestDtoWithRequests =
                itemRequestRepository.findAllByRequestorId(userId).stream()
                        .map(request -> RequestMapper.toRequestDtoWithRequest(request))
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDtoReq item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public List<RequestDtoWithRequest> getAllItemRequest(Long userId, int from, int size) {
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new MethodArgumentNotValidException(HttpStatus.BAD_REQUEST, "неверный параметр пагинации");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        User requestor = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        List<Request> byOwnerId = itemRequestRepository.findByOwnerId(userId, pageable);
        List<RequestDtoWithRequest> requestDtoWithRequests =
                byOwnerId.stream()
                        .map(request -> {
                            return RequestMapper.toRequestDtoWithRequest(request);
                        })
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDtoReq item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public RequestDtoWithRequest getRequestById(Long userId, Long requestId) {
        User requestor = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Request request = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException(HttpStatus.NOT_FOUND, "Запрос предмета по id не найден"));
        RequestDtoWithRequest requestDtoWithRequest = RequestMapper.toRequestDtoWithRequest(request);
        for (ItemDtoReq item : requestDtoWithRequest.getItems()) {
            item.setRequestId(requestId);
        }
        return requestDtoWithRequest;
    }
}
