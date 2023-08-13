package ru.practicum.shareit.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserClientTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserClient userClient;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void createUserWithBadNameTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .name("")
                .email("ivan@mail.ru")
                .build();


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void createUserWithBadEmailTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .name("Ivan")
                .email("@mail.ru")
                .build();


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void createUserWithEmptyEmailTest() throws Exception {
        //fail name
        UserDto userDto = UserDto.builder()
                .name("Ivan")
                .email("")
                .build();


        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}