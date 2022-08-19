package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;


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

    @SneakyThrows
    @PutMapping("/users")
    public User put (@Valid @RequestBody User user) {
        log.info("Получен запрос на изменение данных пользователя");
            return service.update(user);
    }


    @DeleteMapping ("/users")
    public void delete (@Valid @RequestBody User user){
        log.info("Получен запрос на удаление пользователя");
        service.delete(user);
    }

    @SneakyThrows
    @GetMapping("/users/{id}")
    public User find (@PathVariable long id) {
        log.info("Получен запрос на вывод данных одного пользователя");
            return service.find(id);
    }

    @GetMapping ("/users")
    public List<User> findAll()  {
        log.info("Получен запрос на вывод данных всех пользователей");
        return service.findAll();
    }
    @SneakyThrows
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend (@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Получен запрос на добавление друзей");
            return service.addFriend(id, friendId);
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

