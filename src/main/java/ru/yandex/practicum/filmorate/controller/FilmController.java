package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequestMapping
@RestController
public class FilmController  extends Controller<Film>{
    long id = 1;

    @Override
    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film)  throws ValidationException {
        log.info("Фильм создан");
        film.setId(id);
        id++;
        return super.create(film);
    }

    @Override
    @PutMapping("/films")
    public Film put (@Valid @RequestBody Film film) throws ValidationException {
        log.info("Данные фильма обновлены");
        return super.put(film);
    }

    @Override
    @GetMapping("/films")
    public List<Film> findAll()  {
        log.info("Выведен список всех фильмов");
        return super.findAll();
    }
}
