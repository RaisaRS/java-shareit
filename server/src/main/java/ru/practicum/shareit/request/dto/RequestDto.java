package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    @NotBlank(message = "не должно быть пустым")
    private String description;
    private UserDto requestor;
    @JsonProperty("created")
    private LocalDateTime created;
    private List<ItemDtoReq> items;
}
