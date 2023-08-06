package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank(message = "Name не должен быть пустым")
    private String name;
    @NotBlank
    @NotBlank(message = "description не должен быть пустым")
    private String description;
    private Long ownerId;
    @NotNull(message = "available не должен отсутствовать")
    private Boolean available;
    private List<CommentDto> comments;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto)) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(getName(), itemDto.getName()) && Objects.equals(getDescription(), itemDto.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
