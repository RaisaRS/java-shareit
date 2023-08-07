package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Validated
@Builder
public class BookingDto {
    private Long id;
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    @JsonProperty("start")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    @Future(message = "Дата окончания бронирования не может быть в прошлом")
    @JsonProperty("end")
    private LocalDateTime end;
    private long bookerId;
    private Long itemId;
    private Item item;
    private Status status;
}
