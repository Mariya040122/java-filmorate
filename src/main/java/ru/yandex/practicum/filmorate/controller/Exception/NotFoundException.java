package ru.yandex.practicum.filmorate.controller.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(message);
    }
}
