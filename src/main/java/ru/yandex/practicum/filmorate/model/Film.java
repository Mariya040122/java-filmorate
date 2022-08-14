package ru.yandex.practicum.filmorate.model;

import lombok.*;

import ru.yandex.practicum.filmorate.Validator.LumiereDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class Film {

    long id = 0;

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

    MPA mpa;

    List<Genre> genres = new ArrayList<>();
    Set<Long> likes = new HashSet<>();

    public void addLike (long id){
        likes.add(id);
    }

    public void removeLike (long id){
        likes.remove(id);
    }

    public Set<Long> getLikes(){
        return likes;
    }

    public int LikesAmount(){
        return likes.size();
    }
}
