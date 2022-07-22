package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class User {

    long id = 0;

    private Set<Long> friends = new HashSet<>();

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

    public void addFriend (long id){
        friends.add(id);
    }

    public void removeFriend (long id){
        friends.remove(id);
    }

    public Set<Long> getFriends(){
       return friends;
}

}
