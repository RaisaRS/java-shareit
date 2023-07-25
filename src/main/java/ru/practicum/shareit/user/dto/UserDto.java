package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private long id;
    @NotBlank(message = "имя пользователя не моет быть пустым")
    private String name;
    @NotBlank(message = "Email не должен быть пустым")
    @Email
    private String email;
}
