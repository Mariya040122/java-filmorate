package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping
@RestController
public class UserController extends Controller<User>{
    long id = 1;
    @Override
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user)  throws ValidationException {
        log.info("Пользователь создан.");
        if (user.getName() == null || user.getName().replace(" ", "").isEmpty()){
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое!Будет использован логин!");
        }
        user.setId(id);
        id++;
        return super.create(user);
    }

    @Override
    @PutMapping("/users")
    public User put (@Valid @RequestBody User user) throws ValidationException {
        log.info("Данные пользователя обновлены");
        if (user.getName() == null || user.getName().replace(" ", "").isEmpty()){
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое!Будет использован логин!");
        }
        return super.put(user);

    }
        @Override
        @GetMapping("/users")
        public List<User> findAll()  {
            log.info("Выведен список всех пользователей");
            return super.findAll();
        }
    }

