package ru.yandex.practicum.filmorate.Storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    void delete (Film film);

    Film find (long id);

    List<Film> findAll ();

    Film addLike (long id, long userId);

    Film removeLike(long id, long userId);

    List<Film> getLikesAmount (int count);

    boolean isExist(long id);

    boolean containsUserLike(long id, long userId);
}
