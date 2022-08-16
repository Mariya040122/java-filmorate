package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.Storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.Storage.GenreStorage;
import ru.yandex.practicum.filmorate.Storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.controller.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {

    private long id = 1;
    @Autowired
    @Qualifier("GenreDatabase")

    private  GenreStorage storage;

    public void GenreService(@Qualifier("GenreDatabase") GenreStorage storage){
        this.storage = storage;
    }

    public List<Genre> getGenres(){
        return storage.getGenres();
    }

    public Genre getGenresById(long id) throws NotFoundException {
        Genre genre = storage.getGenresById(id);
        if (genre != null){
            return genre;
        } else throw new NotFoundException("Ошибка, данный жанр не найден!");
    }
}
