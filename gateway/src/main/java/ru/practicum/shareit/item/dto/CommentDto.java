package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
