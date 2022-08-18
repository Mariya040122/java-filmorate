package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.Service.FilmService;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping
@RestController
public class FilmController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final FilmService service;
    @Autowired
    public FilmController (FilmService service) {
        this.service = service;
    }


    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        return service.add(film);
    }

    @SneakyThrows
    @PutMapping("/films")
    public Film put (@Valid @RequestBody Film film) {
       log.info("Получен запрос на изменение данных фильма");
            return service.update(film);
    }

    @DeleteMapping ("/films")
    public void delete (@Valid @RequestBody Film film){
        log.info("Получен запрос на удаление фильма");
        service.delete(film);
    }

    @SneakyThrows
    @GetMapping("/films/{id}")
    public Film find(@PathVariable long id){
        log.info("Получен запрос на вывод данных одного фильма");
            return service.find(id);
    }

    @GetMapping ("/films")
    public List<Film> findAll () {
       log.info("Получен запрос на вывод данных всех фильмов");
        return service.findAll();
    }
    @SneakyThrows
    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike (@PathVariable("id") long id, @PathVariable("userId") long userId){
        log.info("Получен запрос на добавление лайка");
            return service.addLike(id, userId);
    }

    @SneakyThrows
    @DeleteMapping ("/films/{id}/like/{userId}")
    public Film removeLike (@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Получен запрос на удаление лайка");
            return service.removeLike(id, userId);
    }

    @GetMapping ("/films/popular")
    public List<Film> getLikesAmount (@RequestParam(defaultValue = "10") int count){
        log.info("Получен запрос на вывод фильмов, по количеству лайков");
        return service.getLikesAmount(count);
    }
}
