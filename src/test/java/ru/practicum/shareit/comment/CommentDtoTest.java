package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentDtoTest {

    User user = new User().builder()
            .id(1L)
            .name("Raisa")
            .email("raisa@mail.ru")
            .build();

    @Test
    public void commentDtoTest() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("Raisa")
                .created(LocalDateTime.now())
                .build();


        assertNotNull(commentDto);
        assertEquals(1L, commentDto.getId());
        assertEquals("text", commentDto.getText());
        assertEquals(user.getName(), commentDto.getAuthorName());
    }
}
