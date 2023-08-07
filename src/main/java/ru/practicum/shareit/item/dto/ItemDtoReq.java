package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoReq {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Long requestId;
    private Boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDtoReq)) return false;
        ItemDtoReq itemDtoReq = (ItemDtoReq) o;
        return Objects.equals(getName(), itemDtoReq.getName()) &&
                Objects.equals(getDescription(), itemDtoReq.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}

