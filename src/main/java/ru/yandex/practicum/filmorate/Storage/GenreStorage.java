package ru.yandex.practicum.filmorate.Storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public List<Genre> getGenres();

    public Genre getGenresById(long id);
}
