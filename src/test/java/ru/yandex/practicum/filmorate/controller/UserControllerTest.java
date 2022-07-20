package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("01.Создание пользователя (код 200)")
    void createUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("02.Создание пользователя с пробелом в логине (код 400)")
    void createFailLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"lo gin\",\n" +
                                "  \"email\": \"email.ru\",\n" +
                                "  \"birthday\": \"2224-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("03.Создание пользователя с не верной датой рождения (код 400)")
    void createUserFailBirthdayTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"lo gin\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"2024-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("04.Создание пользователя с неверным емейл (код 400)")
    void createFailEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \" \",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("05.Обнавление пользователя (код 200)")
    void userUpdateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"login\": \"login\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"email\": \"email@email.ru\",\n" +
                                "  \"birthday\": \"1985-04-01\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("06.Создание пользователя с не верным id, (код 200)")
    void userUpdateUnknownTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
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
    @DisplayName("07.Вывод ползователей (код 200)")
    void getUserAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("08.Создание друга(код 200)")
    void friendCreateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"loginTwo\",\n" +
                                "  \"name\": \"nameTwo\",\n" +
                                "  \"email\": \"email@emailTwo.ru\",\n" +
                                "  \"birthday\": \"1925-04-02\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("loginTwo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nameTwo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@emailTwo.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("1925-04-02"));
    }

    @Test
    @DisplayName("09.Создание общего друга(код 200)")
    void commonFriendCreateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"loginThree\",\n" +
                                "  \"name\": \"nameThree\",\n" +
                                "  \"email\": \"email@emailThree.ru\",\n" +
                                "  \"birthday\": \"1985-04-03\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("loginThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nameThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@emailThree.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("1985-04-03"));
    }

    @Test
    @DisplayName("10. Вывод пользователя (код 200)")
    void userGetTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("11.Вывод неизвестного пользователя(код 404)")
    void userGetUnknownTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("12.Вывод друга(код 200)")
    void friendGetTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("13.Вывод списка друзей(код 200)")
    void userGetFriendsCommonEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)));
    }

    @Test
    @DisplayName("14.Добавление друга(код 200)")
    void userAddFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("15.Добавление неизвестного друга(код 400)")
    void userAddFriendUnknownTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/-1").accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("16.Вывод друзей(код 200)")
    void userGetFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("loginTwo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nameTwo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@emailTwo.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1925-04-02"));
    }

    @Test
    @DisplayName("17.Вывод друга друзей(код 200)")
    void friendGetFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/2/friends").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@email.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-01"));
    }

    @Test
    @DisplayName("18.Вывод собственных друзей (код 200)")
    void userGetFriendsMutualTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)));
    }

    @Test
    @DisplayName("19.Добавление общего друга(код 200)")
    void userAddCommonFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("20.Вывод двух друзей пользователей(код 200)")
    void userGetTwoFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)));
    }

    @Test
    @DisplayName("21.Добавление общего друга(код 200)")
    void friendAddCommonFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/2/friends/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("22.Вывод двух друзей друга (код 200)")
    void friendGetTwoFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/2/friends")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)));
    }

    @Test
    @DisplayName("23.Вывод общих друзей (код 200)")
    void userGetFriendsCommonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)));
    }

    @Test
    @DisplayName("24.Пользователь удаляет друга (код 200)")
    void userRemoveFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/common/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("25.Вывод общих друзей(код 200)")
    void userCommonGetFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@emailThree.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nameThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("loginThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-03"));
    }

    @Test
    @DisplayName("26.Друг выводит общих друзей (код 200)")
    void friendGetFriendsCommonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/2/friends/common/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@emailThree.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nameThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("loginThree"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1985-04-03"));
    }

}