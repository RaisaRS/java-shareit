package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Builder
public class BookingDto {
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime end;
    @JsonProperty(access = WRITE_ONLY)
    private Long itemId;
    private UserDto booker;
    private ItemDto item;
    private Status status;


}
