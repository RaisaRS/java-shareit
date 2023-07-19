package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private User author;
    private LocalDateTime created;
}
