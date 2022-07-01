package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping
@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private List<User> users = new ArrayList<>();


    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        log.info("Добавлен новый пользователь");
        if (user.getEmail().isEmpty() || (!user.getEmail().contains("@"))) {
            log.error("Нет данных электронной почты или отсутствуут символ @!");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин пустой или содеожит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        LocalDate date = LocalDate.now();
        if (user.getBirthday().isAfter(date)) {
            log.error("Указана недопустимая дата рождения!");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName().isEmpty()) {
            log.error("Имя для отображения не может быть пустым!");
            throw new ValidationException("Имя для отображения не может быть пустым.В случае отсутствия имя будет" +
                    " использован логин!");
        }
        this.users.add(user);
        return user;
    } // создание пользователя

    @PutMapping("/users")
    public User putUser (@RequestBody User user) throws ValidationException {
        log.info("Данный пользователя обнавлены");
        if (user.getEmail().isEmpty() || (!user.getEmail().contains("@"))) {
            log.error("Нет данных электронной почты или отсутствуут символ @!");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин пустой или содеожит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        LocalDate date = LocalDate.now();
        if (user.getBirthday().isAfter(date)) {
            log.error("Указана недопустимая дата рождения!");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName().isEmpty()) {
            log.error("Имя для отображения не может быть пустым!");
            throw new ValidationException("Имя для отображения не может быть пустым.В случае отсутствия имя будет" +
                    " использован логин!");
        }
        for (User i : users) {
            if (i.getId() == user.getId()) {
                users.remove(i);
                users.add(user);
                break;
            }
        } return user;
    } // изменеие данных пользователя

    @GetMapping("/users")
    public List<User> findAllUser() {
        log.info("Вывод списка всех пользователей");
        return this.users;
    } // вывод списка всех пользователей

}

