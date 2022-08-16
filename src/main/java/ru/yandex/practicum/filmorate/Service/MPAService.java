package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.GenreStorage;
import ru.yandex.practicum.filmorate.Storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.Storage.MPAStorage;
import ru.yandex.practicum.filmorate.controller.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
@Service
public class MPAService {

    private long id = 1;

    @Autowired
    @Qualifier("MPADatabase")

    private MPAStorage storage;

    public void MPAService(@Qualifier("MPADatabase") MPAStorage storage){
        this.storage = storage;
    }

    public List<MPA> getMPA (){
        return storage.getMPA();
    }

    public MPA getMPAById(long id) throws NotFoundException {
        MPA mpa = storage.getMPAById(id);
        if (mpa != null) {
            return mpa;
        } else throw new NotFoundException("Ошибка, данный рейтинг не найден!");
    }

}
