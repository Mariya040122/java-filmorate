package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.Storage.GenreStorage;
import ru.yandex.practicum.filmorate.controller.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class FilmService  {

    private long id = 1;
    @Autowired
    @Qualifier("FilmDatabase")
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("FilmDatabase") FilmStorage filmStorage, @Qualifier("GenreDatabase") GenreStorage genreStorage){
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
    }

    public Film add(Film film){
        film.setId(id);
        id++;
        Film addedFilm = filmStorage.add(film);
        if (addedFilm.getGenres() != null) {
            film.setGenres(genreStorage.addFilmGenres(addedFilm.getId(), addedFilm.getGenres()));
        }
        return addedFilm;
    }

    public Film update(Film film) throws NotFoundException{
        if (filmStorage.isExist(film.getId())) {
            genreStorage.deleteFilmGenres(film.getId());
            if (film.getGenres() != null) {
                film.setGenres(genreStorage.addFilmGenres(film.getId(), film.getGenres()));
            }
            return filmStorage.update(film);
        } else throw new NotFoundException("Ошибка, при запросе на изменение данных фильма");
    }

    public void delete (Film film){
        genreStorage.deleteFilmGenres(film.getId());
        filmStorage.delete(film);
    }

    public Film find(long id) throws NotFoundException{
        if (filmStorage.isExist(id)) {
            Film film =  filmStorage.find(id);
            List<Genre> genres = genreStorage.getFilmGenreByFilmId(id);
            film.setGenres(genres);
            return film;
        } else throw new NotFoundException ("Ошибка, при запросе вывода данных одного фильма");
    }

    public List<Film> findAll (){
        List<Film> allFilms = filmStorage.findAll();
        for (Film film : allFilms){
            film.setGenres(genreStorage.getFilmGenreByFilmId(film.getId()));
        }
        return allFilms;
    }

    public Film addLike (long id, long userId) throws NotFoundException {
            if (filmStorage.isExist(id)) {
                Film film = filmStorage.addLike(id, userId);
                List<Genre> genres = genreStorage.getFilmGenreByFilmId(film.getId());
                film.setGenres(genres);
                return film;
            } else throw new NotFoundException ("Ошибка, при запросе добавления лайка");
    }

    public Film removeLike (long id, long userId) throws NotFoundException {
        if (filmStorage.isExist(id) && filmStorage.containsUserLike(id, userId)) {
            Film film = filmStorage.removeLike(id, userId);
            List<Genre> genres = genreStorage.getFilmGenreByFilmId(film.getId());
            film.setGenres(genres);
            return film;
        }else throw new NotFoundException ("Ошибка, при запросе удаления лайка");
    }

    public boolean containsUserLike(long id, long userId){
        return filmStorage.containsUserLike(id, userId);
    }

    public List<Film> getLikesAmount(int count){
        List<Film> films = filmStorage.getLikesAmount(count);
        for (Film film : films){
            film.setGenres(genreStorage.getFilmGenreByFilmId(film.getId()));
        }
        return films;
    }

    public boolean isExist(long id){
        return filmStorage.isExist(id);
    }




}
