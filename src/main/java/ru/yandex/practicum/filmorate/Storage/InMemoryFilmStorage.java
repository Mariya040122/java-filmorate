package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("FilmMemory")
public class InMemoryFilmStorage implements FilmStorage{

    private List<Film> films = new ArrayList<>();

    @Override
    public Film add(Film film) {
        films.add(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film present = films.stream()
                .filter(c -> c.getId() == film.getId())
                .findAny()
                .orElse(null);
        if (present != null) {
            films.remove(present);
            films.add(film);
            return film;
        } else return null;
    }

    @Override
    public void delete (Film film){
        Film present = films.stream()
                .filter(c -> c.getId() == film.getId())
                .findFirst()
                .orElse(null);
        films.remove(film);
    }

    @Override
    public Film find (long id){
        Film present = films.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        return present;
    }

    @Override
    public List<Film> findAll (){
        return films;
    }

    @Override
    public Film addLike (long id, long userId){
        Film film = films.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (film != null) {
            film.addLike(userId);
            return film;
        } else return null;
    }

    @Override
    public Film removeLike(long id, long userId) {

        Film film = films.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (film != null) {
            film.removeLike(userId);
            return film;
        } else return null;
    }

    @Override
    public  List<Film> getLikesAmount (int count){
        Collections.sort(films, (f1, f2) -> f2.LikesAmount() - f1.LikesAmount());
        if (count > films.size()){
            count = films.size();
        }
        return films.subList(0, count);
    }

    @Override
    public boolean isExist(long id){
        return (!films.stream()
                .filter(u -> u.getId() == id)
                .collect(Collectors.toList()).isEmpty());
    }

    public boolean containsUserLike(long id, long userId){
        Set<Long> likes = films.stream()
                .filter(u -> u.getId() == id)
                .collect(Collectors.toList()).get(0).getLikes();
        if (likes != null){
            return likes.contains(userId);
        } else return false;
    }

    public static List<Genre> getGenres(){
        return null;
    }

    public static Genre getGenresById(long id){
        return null;
    }

    public static List<MPA> getMPA(){
        return null;
    }

    public static MPA getMPAById(long id){
        return null;
    }
}
