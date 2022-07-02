package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Создание пользователя (код 200)")
    void createUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Создание пользователя с пустыми полями (код 400)")
    void createEmptyUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с пробелом в логине (код 400)")
    void createUserFailLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"lo gin\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с пустым логином (код 400)")
    void createUserEmptyLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \" \",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с пустым именем (код 200)")
    void createUserEmptyNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \" \",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Создание пользователя с пустым Email, (код 400)")
    void createUserFailEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \" \",\n" +
                                "  \"email\": \" \",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с датой рождения позже текущей (код 400)")
    void createUserDateAfterNowTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"2085-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя ДР = now()(код 400)")
    void createUserDateNowTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"2025-07-02\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление данных пользователя (код 200)")
    void updateUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Обновление данных пользователя c несуществующем id (код 200)")
    void updateUserNotIdTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 10,\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"namename\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Вывод списка всех пользователей (код 200)")
    void addTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}