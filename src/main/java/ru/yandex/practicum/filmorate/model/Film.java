package ru.yandex.practicum.filmorate.model;

import lombok.*;

import ru.yandex.practicum.filmorate.Validator.LumiereDate;

import javax.validation.constraints.*;
import java.time.LocalDate;


@AllArgsConstructor
@Getter
@Setter
public class Film extends Model {


    @NotBlank (message = "Название не может быть пустым!")
    String name;

    @NotNull
    @Size(max=200, message = "Максимальная длина описания — 200 символов!")
    String description;

    @NotNull
    @LumiereDate (message = "Дата релиза — должна быть не раньше 28 декабря 1895 года!")
    LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительной!")
    long duration;

}
