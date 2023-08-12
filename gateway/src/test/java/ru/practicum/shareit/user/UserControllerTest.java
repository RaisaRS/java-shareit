package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;



import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.ErrorHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {UserController.class, ErrorHandler.class})
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    private UserDto userToSave;
    private Long userId = 1L;

    @BeforeEach
    public void init() {
        userToSave = UserDto.builder()
                .name("name")
                .email("email@email.com")
                .build();
    }

    @SneakyThrows
    @Test
    public void addUser_Normal() {
        when(userClient.addUser(any())).thenReturn(
                new ResponseEntity<>(new UserDto(), HttpStatus.OK));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void addUser_WrongEmail() {
        userToSave.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        userToSave.setEmail("   ");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    public void addUser_WrongName() {
        userToSave.setName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        userToSave.setName(" ");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    public void updateUser_Normal() {
        when(userClient.updateUser(anyLong(), any())).thenReturn(
                new ResponseEntity<>(new UserDto(), HttpStatus.OK));

        UserDto updatedUser = UserDto.builder()
                .email("otheremail@email.com")
                .build();

        mockMvc.perform(patch("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());

        updatedUser = UserDto.builder()
                .name("new name")
                .build();

        mockMvc.perform(patch("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void updateUser_wrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(patch("/users/" + wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).updateUser(anyLong(), any());
    }

    @SneakyThrows
    @Test
    public void getUser_Normal() {
        when(userClient.getUser(anyLong())).thenReturn(
                new ResponseEntity<>(new UserDto(), HttpStatus.OK));

        mockMvc.perform(get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getUser_WrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(get("/users/" + wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).getUser(anyLong());
    }

    @SneakyThrows
    @Test
    public void getAllUser_Normal() {
        when(userClient.getUser(anyLong())).thenReturn(
                new ResponseEntity<>(List.of(new UserDto()), HttpStatus.OK));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteUser_Normal() {
        when(userClient.deleteUser(anyLong())).thenReturn(
                new ResponseEntity<>(new UserDto(), HttpStatus.OK));

        mockMvc.perform(delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteUser_WrongUserId() {
        Long wrongUserId = -999L;

        mockMvc.perform(delete("/users/" + wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).deleteUser(anyLong());
    }
}
