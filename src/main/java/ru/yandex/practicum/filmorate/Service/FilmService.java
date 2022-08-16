package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.controller.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;


@Service
public class FilmService  {

    private long id = 1;
    @Autowired
    @Qualifier("FilmDatabase")
    private final FilmStorage storage;

    public FilmService(@Qualifier("FilmDatabase") FilmStorage storage){
        this.storage = storage;
    }

    public Film add(Film film){
        film.setId(id);
        id++;
        return storage.add(film);
    }

    public Film update(Film film) throws NotFoundException{
        if (storage.update(film) != null) {
            return storage.update(film);
        } else throw new NotFoundException("Ошибка, при запросе на изменение данных фильма");
    }

    public void delete (Film film){
        storage.delete(film);
    }

    public Film find(long id) throws NotFoundException{
        if (storage.isExist(id)) {
            return storage.find(id);
        } else throw new NotFoundException ("Ошибка, при запросе вывода данных одного фильма");
    }

    public List<Film> findAll (){
        return storage.findAll();
    }

    public Film addLike (long id, long userId) throws NotFoundException {
            if (storage.isExist(id)) {
                return storage.addLike(id, userId);
            } else throw new NotFoundException ("Ошибка, при запросе добавления лайка");
    }

    public Film removeLike (long id, long userId) throws NotFoundException {
        if (storage.isExist(id) && storage.containsUserLike(id, userId)) {
            return storage.removeLike(id, userId);
        }else throw new NotFoundException ("Ошибка, при запросе удаления лайка");
    }

    public boolean containsUserLike(long id, long userId){
        return storage.containsUserLike(id, userId);
    }

    public List<Film> getLikesAmount(int count){
        return storage.getLikesAmount(count);
    }

    public boolean isExist(long id){
        return storage.isExist(id);
    }




}
