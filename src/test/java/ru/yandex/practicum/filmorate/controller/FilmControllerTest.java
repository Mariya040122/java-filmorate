package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.Service.FilmService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("01.Вывод списка всех фильмов (код 200)")
    void filmGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("02.Создание нового фильма (код 200)")
    void createFilmTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": \"120\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("03.Создание фильма c не верным названием (код 400)")
    void filmCreateFailNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"name\": \" \",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"duration\": \"120\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("04.Создание фильма с описанием больше 200 символов (код 400)")
    void createFilmDescriptionSize200Test() throws Exception {
        byte[] bytes = new byte[201];
        Arrays.fill(bytes, (byte) 'A');
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
    @DisplayName("05.Создание фильма c датой релиза до 28 декабря 1895 года (код 400)")
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
    @DisplayName("06.Создание фильма с не верной продолжительностью (код 400)")
    void filmCreateFailDurationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod4\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1895-12-27\",\n" +
                                "  \"duration\": -120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("07. Внесение изменений в данные фильма (код 200)")
    void updateFilmTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"description\": \"new description\",\n" +
                                "  \"duration\": 120,\n" +
                                "  \"rate\": 10\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("08. Внесение не корректных изменений в данные фильма (код 404)")
    void filmUpdateUnknownTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": -1,\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"releaseDate\": \"1985-04-01\",\n" +
                                "  \"description\": \"new description\",\n" +
                                "  \"duration\": 120,\n" +
                                "  \"rate\": 10\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("09.Вывод популярных фильмов(код 200)")
    void filmGetPopularTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("new description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("1985-04-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value("120"));
    }

    @Test
    @DisplayName("10.Создания фильма друга (код 200)")
    void filmCreateFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod4\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1995-12-27\",\n" +
                                "  \"duration\": 120\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nisi eiusmod4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("adipisicing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("1995-12-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value("120"));
    }

    @Test
    @DisplayName("11.Вывод фильма (код 200)")
    void filmGetTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(6)));
    }

    @Test
    @DisplayName("12.Вывод фильма неверный(код 400)")
    void filmGetUnknownTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("13.Добавление лайка к фильму (код 200)")
    void filmAddLikeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    @DisplayName("14.Вывод популярных фильмов(код 200)")
    void filmGetPopularCountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nisi eiusmod4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("adipisicing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("1995-12-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value("120"));
    }

    @Test
    @DisplayName("15.Удаления лайка (код 200)")
    void filmRemoveLikeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/films/2/like/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }


    @Test
    @DisplayName("16.Вывод популярных фильмов (код 200)")
    void filmGetPopularCountTwoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)));
    }

    @Test
    @DisplayName("17.Удаления лайка (код 404)")
    void filmRemoveLikeNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/-2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
