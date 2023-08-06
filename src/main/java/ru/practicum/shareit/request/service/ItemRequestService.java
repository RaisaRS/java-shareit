package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;

import java.util.List;

@Service
public interface ItemRequestService {
    RequestDto addItemRequest(RequestDto requestDto, Long userId);

    List<RequestDtoWithRequest> getItemRequest(Long userId);

    List<RequestDtoWithRequest> getAllItemRequest(Long userId, int from, int size);

    RequestDtoWithRequest getRequestById(Long userId, Long requestId);
}
