package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.validation.Validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank(groups = {Validation.Post.class}, message = "Name не должен быть пустым")
    private String name;
    @NotBlank(groups = {Validation.Post.class}, message = "description не должен быть пустым")
    private String description;
    @NotNull(groups = {Validation.Post.class}, message = "available не должен отсутствовать")
    private Boolean available;
    private List<CommentDto> comments;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private Long requestId;

}
