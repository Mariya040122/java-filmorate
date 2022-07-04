package ru.yandex.practicum.filmorate.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends Exception{

    public ValidationException(String message) {
        super(message);
    }
}
