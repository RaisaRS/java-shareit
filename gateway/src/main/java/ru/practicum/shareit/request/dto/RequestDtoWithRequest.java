package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RequestDtoWithRequest {
    private Long id;
    private String description;
    //private UserDto requestor;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime created;
    private List<ItemDtoReq> items;
}
