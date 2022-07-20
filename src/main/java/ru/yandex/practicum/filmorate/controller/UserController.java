package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping
@RestController
public class UserController {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService service;

    @Autowired
    public UserController (UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user){
        log.info("Получен запрос на добавление пользователя");
        return service.add(user);
    }


    @PutMapping("/users")
    public User put (@Valid @RequestBody User user) throws NotFoundException{
        log.info("Получен запрос на изменение данных пользователя");
        if (service.isExist(user.getId())) {
            return service.update(user);
        } else throw new NotFoundException ("Ошибка");
    }


    @DeleteMapping ("/users")
    public void delete (@Valid @RequestBody User user){
        log.info("Получен запрос на удаление пользователя");
        service.delete(user);
    }


    @GetMapping("/users/{id}")
    public User find (@PathVariable long id)  throws NotFoundException{
        log.info("Получен запрос на вывод данных одного пользователя");
        if (service.isExist(id)) {
            return service.find(id);
        } else throw new NotFoundException ("Ошибка");
    }

    @GetMapping ("/users")
    public List<User> findAll()  {
        log.info("Получен запрос на вывод данных всех пользователей");
        return service.findAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend (@PathVariable("id") long id, @PathVariable("friendId") long friendId)
            throws NotFoundException{
        log.info("Получен запрос на добавление в список друзей");
        if (service.isExist(id) && service.isExist(friendId)) {
            return service.addFriend(id, friendId);
        } else throw new NotFoundException ("Ошибка");
    }

    @DeleteMapping ("/users/{id}/friends/{friendId}")
    public User removeFriend (@PathVariable("id") long id, @PathVariable("friendId") long friendId){
        log.info("Получен запрос на удаление из списка друзей");
       return service.removeFriend(id, friendId);
    }

    @GetMapping ("/users/{id}/friends")
    public List<User> getFriends (@PathVariable("id") long id){
        log.info("Получен запрос на вывод списка всех друзей");
        return service.getFriends(id);
    }

    @GetMapping ("/users/{id}/friends/common/{otherId}")
    public List<User> commonFriendsList (@PathVariable("id") long id,  @PathVariable("otherId") long otherId){
        log.info("Получен запрос на вывод списока друзей, общих с другим пользователем");
        return service.commonFriendsList( id, otherId);
    }

}

