package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Service.FilmService;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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

    @PutMapping("/films")
    public Film put (@Valid @RequestBody Film film) throws NotFoundException{
       log.info("Получен запрос на изменение данных фильма");
        if (service.update(film) != null) {
            return service.update(film);
        } else throw new NotFoundException ("Ошибка");
    }

    @DeleteMapping ("/films")
    public void delete (@Valid @RequestBody Film film){
        log.info("Получен запрос на удаление фильма");
        service.delete(film);
    }


    @GetMapping("/films/{id}")
    public Film find(@PathVariable long id)  throws NotFoundException {
        log.info("Получен запрос на вывод данных одного фильма");
        if (service.isExist(id)) {
            return service.find(id);
        } else throw new NotFoundException ("Ошибка");
    }

    @GetMapping ("/films")
    public List<Film> findAll () {
       log.info("Получен запрос на вывод данных всех фильмов");
        return service.findAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike (@PathVariable("id") long id, @PathVariable("userId") long userId)
            throws NotFoundException{
        log.info("Получен запрос на добавление лайка");
        if (service.isExist(id)) {
            return service.addLike(id, userId);
        } else throw new NotFoundException ("Ошибка");
    }

    @DeleteMapping ("/films/{id}/like/{userId}")
    public Film removeLike (@PathVariable("id") long id, @PathVariable("userId") long userId) throws NotFoundException{
        log.info("Получен запрос на удаление лайка");
        if (service.isExist(id) && service.containsUserLike(id, userId)) {
            return service.removeLike(id, userId);
        }else throw new NotFoundException ("Ошибка");
    }

    @GetMapping ("/films/popular")
    public List<Film> getLikesAmount (@RequestParam(defaultValue = "10") int count){
        log.info("Получен запрос на вывод фильмов, по количеству лайков");
        return service.getLikesAmount(count);
    }

}
