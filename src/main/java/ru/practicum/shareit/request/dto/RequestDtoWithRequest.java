package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class RequestDtoWithRequest {
    private Long id;
    private String description;
    private UserDto requestor;
    @JsonProperty("created")
    private LocalDateTime created;
    private List<ItemDtoReq> items = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDtoWithRequest)) return false;
        RequestDtoWithRequest that = (RequestDtoWithRequest) o;
        return getId().equals(that.getId()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

}
