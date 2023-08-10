package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private LocalDateTime created;
    private String authorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getText(),
                that.getText()) && Objects.equals(getAuthorName(), that.getAuthorName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getAuthorName());
    }
}
