package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.GenreService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequestMapping
@RestController
public class GenreController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GenreService service;

    @Autowired
    public GenreController (GenreService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public List<Genre> getGenres(){
        log.info("Получен запрос на вывод всех жанров");
        return service.getGenres();
    }
    @SneakyThrows
    @GetMapping ("/genres/{id}")
    Genre getGenresById(@PathVariable("id") long id){
        log.info("Получен запрос на вывод всех жанра по id");
        return service.getGenresById(id);
    }
}
