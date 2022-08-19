package ru.yandex.practicum.filmorate.Storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {

    public List<MPA> getMPA();

    public MPA getMPAById(long id);

}
