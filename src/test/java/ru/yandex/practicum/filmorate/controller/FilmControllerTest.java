package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Создание нового фильма (код 200)")
    void createFilmTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Создание фильма с пустыми полями (код 400)")
    void createFilmEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма с пустым именем (код 400)")
    void createFilmEmptyNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма с описанием в 200 символов (код 200)")
    void createFilmDescriptionSize200Test() throws Exception {
        byte[] bytes = new byte[200];
		Arrays.fill(bytes, (byte)'A');
		String desc = new String(bytes);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"" +
                                desc +
                                "\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Создание фильма с описанием в 201 символ (код 400)")
    void createFilmDescriptionSize201Test() throws Exception {
        byte[] bytes = new byte[201];
        Arrays.fill(bytes, (byte)'A');
        String desc = new String(bytes);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"" +
                                desc +
                                "\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма c датой релиза после 28 декабря 1895 год (код 200)")
    void createFilmReleaseDate28121895Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"releaseDate\": \"1895-12-29\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Создание фильма c датой релиза до 28 декабря 1895 года (код 200)")
    void createFilmReleaseDate27121895Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod4\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1895-12-27\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление данных фильма (код 200)")
    void updateFilmTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 0,\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"description\": \"new description\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Вывод списка всех фильмов (код 200)")
    void addTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}