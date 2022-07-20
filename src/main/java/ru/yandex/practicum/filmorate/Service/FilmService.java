package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


@Service
public class FilmService  {

    private long id = 1;
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage){
        this.storage = storage;
    }

    public Film add(Film film){
        film.setId(id);
        id++;
        return storage.add(film);
    }

    public Film update(Film film){
        return storage.update(film);
    }

    public void delete (Film film){
        storage.delete(film);
    }

    public Film find(long id){
       return storage.find(id);
    }

    public List<Film> findAll (){
        return storage.findAll();
    }

    public Film addLike (long id, long userId){
        return storage.addLike(id, userId);
    }

    public Film removeLike (long id, long userId){
        return storage.removeLike(id, userId);
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
