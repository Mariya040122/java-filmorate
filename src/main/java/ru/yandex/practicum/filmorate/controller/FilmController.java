package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequestMapping
@RestController
public class FilmController {

    private long id = 1;

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private List<Film> films = new ArrayList<>();

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film)  throws ValidationException {
        log.info("Фильм создан");
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Не внесено название фильма!");
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.error("Длина описания фильма больше 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse("28.12.1895", formatter);
        if (film.getReleaseDate().isBefore(date)) {
            log.error("Дата релиза фильма раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза — должна быть не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть отрицательным числом!");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        film.setId(id);
        films.add(film);
        id++;
        return film;

    } // создание фильма

    @PutMapping("/films")
    public Film putFilm (@RequestBody Film film) throws ValidationException {
        log.info("Фильм обновлен");
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Не внесено название фильма!");
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.error("Длина описания фильма больше 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse("28.12.1895", formatter);
        if (film.getReleaseDate().isBefore(date)) {
            log.error("Дата релиза фильма раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза — должна быть не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть отрицательным числом!");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        if (film.getId() < 1) {
            throw new RuntimeException();
        }
        for (Film i : films) {
            if (i.getId() == film.getId()) {
                films.remove(i);
                films.add(film);
                break;
            }
        } return film;
    } // изменение фильма

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        log.info("Вывод списка всех фильмов");
        return this.films;
    } //вывод списка всех фильмов
}
