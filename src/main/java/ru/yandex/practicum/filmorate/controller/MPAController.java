package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Service.GenreService;
import ru.yandex.practicum.filmorate.Service.MPAService;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@RequestMapping
@RestController
public class MPAController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MPAService service;

    @Autowired
    public MPAController(MPAService service){
        this.service = service;
    }


    @GetMapping("/mpa")
    public List<MPA> getMPA (){
        log.info("Получен запрос на вывод всех рейтингов");
        return service.getMPA();
    }

    @SneakyThrows
    @GetMapping ("/mpa/{id}")
    public MPA getMPAById(@PathVariable("id") long id){
        log.info("Получен запрос на вывод рейтинга по id");
        return service.getMPAById(id);
    }
}
