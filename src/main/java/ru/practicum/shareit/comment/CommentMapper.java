package ru.practicum.shareit.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(User user, Item item, CommentDto commentDto, LocalDateTime created) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setCreated(created);
       return comment;
    }
}
