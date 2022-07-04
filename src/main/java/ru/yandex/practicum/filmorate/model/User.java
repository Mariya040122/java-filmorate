package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;

import java.time.LocalDate;


@AllArgsConstructor
@Getter
@Setter
public class User extends Model{

    @Email (message = "Электронная почта не может быть пустой и должна содержать символ @!")
    String email;

    @NotBlank (message = "Логин не может быть пустым!")
    @Pattern(
            regexp = "[^\\s]+",
            message = "Логин не может содержать пробелы!"
    )
    String login;

    String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;

}
