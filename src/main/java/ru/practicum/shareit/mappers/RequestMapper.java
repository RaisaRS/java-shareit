package ru.practicum.shareit.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class RequestMapper {
    public static Request toItemRequest(User user, RequestDto requestDto) {
        return Request.builder()
                .description(requestDto.getDescription())
                .requestor(user)
                .build();
    }

    public static RequestDto toItemRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }
}
